# btest

## Architecture
Based on [Clean Architecture](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
and hexagonal architecture.

![alt text](https://blog.cleancoder.com/uncle-bob/images/2012-08-13-the-clean-architecture/CleanArchitecture.jpg)

## Language
* [Kotlin](https://kotlinlang.org/) (JDK11)

## Docker
Running gradle task `bootBuildImage` will generate the docker image `btest:1.0.0`.
When the task is finished run command `docker run --rm -it -p 8080:8080 btest:1.0.0`.

The postman collection is included in this [link](https://www.getpostman.com/collections/f943e402d614ec06409f).
