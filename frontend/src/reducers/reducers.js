import {RECEIVE_PROFIT} from "../actions/action.js";
import {CHANGE_AMOUNT, CHANGE_DATE} from "../actions/action";

function requestProfit(state = {
    purchaseDate: '2019-01-01',
    amount: 100
}, action) {
    switch (action.type) {
        case CHANGE_DATE:
            return Object.assign({}, state, {
                purchaseDate: action.date
            });
        case CHANGE_AMOUNT:
            return Object.assign({}, state, {
                amount: action.amount
            });
        case RECEIVE_PROFIT:
            return Object.assign({}, state, {
                profit: action.profit,
                purchaseDate: action.purchaseDate,
                amount: action.amount
            });
        default:
            return state;
    }
}

const rootReducer = requestProfit;

export default rootReducer;