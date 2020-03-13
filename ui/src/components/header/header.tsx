import * as React from "react";

type Props {
    title: String
}

function Menue(props: Props) {

    return (
        <div className="navbar is-inline-flex is-transparent">
            <div className="navbar-brand">
                <a className="navbar-item">
                    {props.title}
            </a>
            </div>
            <div className="navbar-menu">
                <div className="navbar-item">
                </div>
            </div>
            <div className="navbar-item is-flex-touch">
                <a className="navbar-item">
                    <i className="material-icons">person_outline</i>
                </a>
            </div>
        </div>
    );
}

export default Menue