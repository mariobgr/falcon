class WebSocketController {

    constructor() {
        this._onConnected = this._onConnected.bind(this);
    }

    _onConnected(frame) {
        this.setConnected(true);
        console.log('Connected: ' + frame);
        this.stompClient.subscribe('/topic/falcon', this.showMessage);
    }

    setConnected(connected) {
        document.getElementById('connect').disabled = connected;
        document.getElementById('disconnect').disabled = !connected;
        document.getElementById('mural').style.visibility = connected ? 'visible' : 'hidden';
        document.getElementById('response').innerHTML = '';
    }

    connect() {
        var socket = new SockJS('/websocket-app');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, this._onConnected);
    }

    disconnect() {
        if(this.stompClient != null) {
            this.stompClient.disconnect();
        }
        this.setConnected(false);
        console.log("Disconnected");
    }

    sendMessage() {

        var message = document.getElementById('text').value;
        var timestamp = Math.floor(Date.now() / 1000);
        var random = Math.floor(Math.random() * 100);

        var json = { "message" : message, "timestamp" : timestamp, "random" : random };

        var xhr = new XMLHttpRequest();
        xhr.open('POST', '/api/sendRequest', true);
        xhr.setRequestHeader("Content-Type", "application/json");
        xhr.onload = function () {};
        xhr.send(JSON.stringify(json));

        //this.stompClient.send("/app/message", {}, JSON.stringify(json));

    }

    showMessage(message) {
        var response = document.getElementById('response');
        var p = document.createElement('p');
        p.style.wordWrap = 'break-word';
        console.log(message);
        p.appendChild(document.createTextNode(message.body));
        response.appendChild(p);
    }

    getAll() {

        var page = parseInt(document.getElementById('page').value);

        var xhr = new XMLHttpRequest();
        xhr.open('GET', '/api/getAll?page=' + page, true);
        xhr.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
        xhr.onload = function () {

            var response = document.getElementById('backlog-response');

            var message = xhr.response;
            var json = JSON.parse(message);

            for (var i = 0; i < json.length; i++) {

                var p = document.createElement('p');
                p.style.wordWrap = 'break-word';

                var obj = json[i];

                p.appendChild(document.createTextNode(
                    "MessageModel{" +
                    "id='" + obj.id + '\'' +
                    ", message='" + obj.message + '\'' +
                    ", random=" + obj.random +
                    ", timestamp='" + obj.timestamp + '\'' +
                    '}'
                ));

                response.appendChild(p);

            }

            response.appendChild(document.createElement('hr'));

        };
        xhr.send();

    }

}
var webSocket = new WebSocketController();