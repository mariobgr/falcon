# Falcon Test Task

This project is a test task - mostly just to get acquainted with some new technologies. It includes a few major components:

  - SpringBoot
  - RabbitMQ
  - Redis
  - MySQL (using jdbcTemplate)
  - STOMP Protocol & Websockets
  - Magic.
  

### What does this project do:

  - It has two API endpoints - one for pushing a sample JSON through the queues, and another - for getting all the data out of the MySQL database
  - The moment a JSON request arrives at the API endpoint, it's stringified content is pushed into a Redis server with an *unique* key
  - There is a @Scheduled task that pumps automated messages into the queue, but those messages are deliberately not allowed in the Redis
  - Both API endpoints are available through a (not so) friendly interface at [http://localhost:8080](http://localhost:8080)

### So how should I build this project?

For maximum ease and convenience (and bonus points), I made sure it can be started using only two simple lines in the Terminal:
```sh
$ docker-compose up -d
$ mvn spring-boot:run
```
I guess the fear I had from Docker was completely unnecessary - it turned out to be a great tool, easy to use, that helps you keep your computer a less messy place.

### Anything special?

There are a few things I would like to point out, since I decided they would be good ideas for this task:
  - Since we are dealing with a REST API here, we know people tend to abuse those (especially when free). That's why it has a LimitFilter, a Java class that keeps people doing more than 10 requests for a given time away with  response code 429 (Too Many Requests)
  - It is not explicitly written in the task, but I believe anyone who doesn't take measures against DB-killer queries should be punished. That's why the API endpoint that gathers all the rows from the MySQL table has a limit of 20 rows per page. Luckily, the (terrible) user interface allows us to change the page with ease
  - I experimented a lot, pushing broken JSON and empty values into those queues and caches. In the end, I decided that a broken JSON or empty values should not be allowed into our datastore. The MessageModel ignores unknown JsonProperties and let's go if parsing a shitty JSON turns out to be too heavy task - we got plenty of those
  - Every new message is pushed with ID = -1. It gets its own unique ID only after making it to the MySQL Server, that takes care of the auto-increment. Speaking of database operations - except for pushing messages until we run out of disk space - we can't really manipulate the data in the MySQL (or anywhere, to be fair) - but I decided that going there would be an overkill.

The only part I decided not to commit is the tests. I gave them a try, just to complete every single point in the task... but it did not work out very well. My imagination got lazy like a programmer after a big lunch and I came up with only 3 lame tests. It would be nice if I give them a bit more attention in the future.

### Last words

  - The whole project was started and finished in less than 24h (the commit history is available on github). Not deliberately.
  - The whole project was coded on a Mid-2009 MacBook Pro 17 (one build took ~40 sec). Again, not deliberately.
  - The whole project was coded while listening to Serbian turbo-folk - deliberately. That tends to boost my productivity with at least 20-30%, depending on the song :)

Well... that's all, folks. I hope we get to see each other again :)

M.