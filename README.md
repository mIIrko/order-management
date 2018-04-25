[![pipeline status](https://gitlab.in.htwg-konstanz.de/mibay/swqs-order-management/badges/master/pipeline.svg)](https://gitlab.in.htwg-konstanz.de/mibay/swqs-order-management/commits/master)
[![coverage report](https://gitlab.in.htwg-konstanz.de/mibay/swqs-order-management/badges/master/coverage.svg)](https://gitlab.in.htwg-konstanz.de/mibay/swqs-order-management/commits/master)


# swqs-order-management
This component manages the creation of a new order. It consists of multiple sub components:
* **Shipping Cost Service**: Calculates the shipping costs for an order depending on the number of items.
* **Email Service**: Send a confirmation mail to the customer when a new order was created.
* **Currency Converter Service**: In the core the shop works with euro's. Customer can choose in which currency they want the order. If they want another currency, this service is used to obtain a current exchange rate.
* **Payment Method Service**: ..****.