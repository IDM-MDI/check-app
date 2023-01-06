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
* *Docker*

### Entity description
* **Product** - item for sale which can be on offer
* **Discount Card** - card that can be used to get a discount

### Entity in database
* **Product**

|          Name |     Type     | Default |
|--------------:|:------------:|:-------:|
|          `id` |     long     | nothing |
|        `name` | varchar(255) | nothing |
|       `price` |   numeric    | nothing |
| `is_on_offer` |   boolean    |  false  |

* **Discount_Card** 

|                    Name |  Type   | Default |
|------------------------:|:-------:|:-------:|
|                    `id` |  long   | nothing |
|       `discount_number` | integer | nothing |
|              `discount` | integer | nothing |

### How to run
* Install docker
* Execute command **docker-compose up** in root project

### Endpoints
## Check
> URL: /api/v1/check
> METHOD: GET <br/>

**Query Parameters**

|   Name | Required |   Type    | Description                                                                                                                     |
|-------:|:--------:|:---------:|---------------------------------------------------------------------------------------------------------------------------------|
|   `id` | required | integer[] | ID of products to be purchased. If the ID is empty, then the server will throw a service exception (Entity not found).          |
| `card` | optional |  integer  | Discount card number. If it is empty, then the card will not be applied. If multiple cards are specified, one will be selected. |

**Response**

```
// CheckModel
{
    "elements": [
        {
            "product": {
                "id": 1,
                "name": "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
                "offer": true,
                "price": 109.95
            },
            "count": 1,
            "totalPrice": 109.95
        },
        {
            "product": {
                "id": 7,
                "name": "DANVOUY Womens T Shirt Casual Cotton Short",
                "offer": true,
                "price": 12.99
            },
            "count": 8,
            "totalPrice": 93.528
        },
        {
            "product": {
                "id": 2,
                "name": "Mens Casual Premium Slim Fit T-Shirts",
                "offer": false,
                "price": 22.3
            },
            "count": 1,
            "totalPrice": 22.3
        }
    ],
    "discountCard": {
        "id": 1,
        "number": 1500,
        "discount": 15
    },
    "totalPriceWithoutCard": 225.77800000000002,
    "totalPrice": 191.9113,
    "createdTime": "2022-12-24T16:04:36.8236562"
}
```
## Product
> URL: /api/v1/product/{id}
> METHOD: GET <br/>

**Parameters**

|   Name | Required | Type | Description                                                 |
|-------:|:--------:|:----:|-------------------------------------------------------------|
|   `id` | required | long | Product ID. The product model will be returned in response. |


> URL: /api/v1/product
> METHOD: POST <br/>

**Parameters**

|    Name | Required |  Type   | Description                                                                                                                                 |
|--------:|:--------:|:-------:|---------------------------------------------------------------------------------------------------------------------------------------------|
|  `name` | required | string  | The product's name. Required length up to 255 characters.                                                                                   |
| `price` | required | double  | Product price.Minimum price: 1.                                                                                                             |
| `offer` | optional | boolean | Is this product on offer? If at the time of purchase, this product will be in quantities of 5 or more, then a 10% discount will be applied. |

> URL: /api/v1/product/{id}
> METHOD: DELETE <br/>

**Parameters**

|   Name | Required | Type | Description                                                 |
|-------:|:--------:|:----:|-------------------------------------------------------------|
|   `id` | required | long | ID of the product to be removed. The response is a string about successful deletion. |

**Response**

```
// ProductModel
{
    "product": {
                "id": 1,
                "name": "Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops",
                "offer": true,
                "price": 109.95
    }
}
```
## Discount Card
> URL: /api/v1/card/{id}
> METHOD: GET <br/>

**Parameters**

|   Name | Required | Type | Description                                           |
|-------:|:--------:|:----:|-------------------------------------------------------|
|   `id` | required | long | Card ID. The card model will be returned in response. |


> URL: /api/v1/card
> METHOD: POST <br/>

**Parameters**

|       Name | Required |  Type   | Description                                                                                                                                 |
|-----------:|:--------:|:-------:|---------------------------------------------------------------------------------------------------------------------------------------------|
|   `number` | required | integer | The card number to be used when making a purchase. Minimum number: 1                                                                                   |
| `discount` | required | integer | Specifies how many percent of the discount will apply to the purchase.                                                                                                              |

> URL: /api/v1/card/{id}
> METHOD: DELETE <br/>

**Parameters**

|   Name | Required | Type | Description                                                                                |
|-------:|:--------:|:----:|--------------------------------------------------------------------------------------------|
|   `id` | required | long | ID of the discount card to be removed. The response is a string about successful deletion. |

**Response**

```
// ProductModel
{
    "discountCard": {
        "id": 1,
        "number": 1500,
        "discount": 15
    }
}
```
