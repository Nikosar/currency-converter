export const RECEIVE_PROFIT = 'RECEIVE_PROFIT';
export const CHANGE_DATE = 'CHANGE_DATE';
export const CHANGE_AMOUNT = 'CHANGE_AMOUNT';


export function changeDate(date) {
    console.log(date);
    return {
        type: CHANGE_DATE,
        date: date
    }
}

export function changeAmount(amount) {
    return {
        type: CHANGE_AMOUNT,
        amount: amount
    }
}

export function receive(purchaseDate, amount, json) {
    return {
        type: RECEIVE_PROFIT,
        purchaseDate,
        amount,
        profit: json
    }

}

export function fetchProfit(purchaseDate, amount) {
    return function (dispatch) {

        return fetch(`/calculate?&date=` + purchaseDate + `&amount=` + amount)
            .then(response => response.json(), error => console.log('error', error))
            .then(json => dispatch(receive(purchaseDate, amount, json)));
    }
}