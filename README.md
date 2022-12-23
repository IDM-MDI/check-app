# Check Application API
![Clevertec-logo](https://sortlist-core-api.s3.eu-west-1.amazonaws.com/anq21gq3yg16iedrippbqixybqkb)

---

The application was created for writing a check from product list and a discount card.

### Used technologies
* *Java 17*
* *Spring Boot*
* *Spring Data JPA*
* *PostgreSQL*
* *H2Database(Test)*
* *JUnit*
* *Mockito*
* *Lombok*

### Endpoints
> ## Check
> [/api/v1/check]

### Entity description
* **Product** - item for sale which can be on offer
* **Discount Card** - card that can be used to get a discount

### Entity in database
* **Product** {
    * id - bigint
    * name - varchar(255)
    * price - numeric
    * is_on_offer - boolean = false <br/>
      }
* **Discount_Card** {
    * id - bigint
    * discount_number - integer
    * discount - integer <br/>
      }

### How to run
* Install docker
* Execute command **docker-compose up** in root project