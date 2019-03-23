import React from 'react';
import {changeAmount, changeDate, fetchProfit} from "../actions/action";
import {connect} from "react-redux";

class CalculatorForm extends React.Component{
    constructor(props) {
        super(props);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleDateChange = this.handleDateChange.bind(this);
        this.handleAmountChange = this.handleAmountChange.bind(this);
    }

    handleDateChange(e) {
        const {dispatch, changeDate} = this.props;
        dispatch(changeDate(e.target.value));
    }
    handleAmountChange(e) {
        const {dispatch, changeAmount} = this.props;
        dispatch(changeAmount(e.target.value));
    }

    handleSubmit(e) {
        e.preventDefault();
        const {fetchProfit, purchaseDate, amount} = this.props;
        fetchProfit(purchaseDate, amount);
    }

    render() {
        const {purchaseDate, amount, profit} = this.props;

        return <div className="calculator_form">
            <form className="calculate_form" onSubmit={this.handleSubmit}>
                <label htmlFor="date">Date</label>
                <input name="purchaseDate" className="date" type="date" value={purchaseDate} onChange={this.handleDateChange}/>
                <label htmlFor="amount">Amount</label>
                <input name="amount" type="number" value={amount ? amount + ' USD': amount} onChange={this.handleAmountChange}/>
                <button type="submit">Recalculate</button>
                <label htmlFor="amount">Profit</label>
                <input readOnly="true" value={profit ? profit.toFixed(2) + " RUB" : profit}/>
            </form>
        </div>;
    }
}

const mapDispatchToProps = dispatch => ({
    fetchProfit: ((purchaseDate, amount) => dispatch(fetchProfit(purchaseDate, amount))),
    changeAmount: (amount => dispatch(changeAmount(amount))),
    changeDate: (date => dispatch(changeDate(date))),
    dispatch: dispatch
});

const mapStateToProps = (state) => ({
    purchaseDate: state.purchaseDate,
    amount: state.amount,
    profit: state.profit
});

export default connect(mapStateToProps, mapDispatchToProps)(CalculatorForm);