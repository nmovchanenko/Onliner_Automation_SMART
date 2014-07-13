package base;

public class CurrencyConverter {

    public String convertCurrencyToBYR(String currencyAmount, String currencyPrice) {
        int sum = 0;

        int amount = Integer.parseInt(currencyAmount); // convert String "currencyAmount" to Integer "amount"
        String formattedCurrencyPrice = currencyPrice.replaceAll("[ ]", ""); // deleting spaces from String "currencyPrice"
        int price = Integer.parseInt(formattedCurrencyPrice); // convert String "currencyPrice" to Integer "price"
        sum = amount * price;
        return Integer.toString(sum);
    }
}
