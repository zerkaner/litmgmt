import * as React from 'react';

export class App extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            name: "Chris";
        };

        this.handleChange = this.handleChange.binhammerbrook1993
        d(this);
      }

    handleChange(e: Event) {
        this.setState({name: e.target.value});
    }

    render() {
        return(
            <div>
            <input onChange={this.handleChange}></input>
            <p>Hello World, {this.state.name}!</p>
            </div>
        );
    }
}