import React from "react";

function LoginForm() {

    return (
        <>
            <div className="container has-text-centered">
            <div className="column is-4 is-offset-4">
                <h4 className="title has-text-black">Login</h4>
                <hr/>
                <p className="subtitle has-text-black">Please login to proceed.</p>
                <div className="box">
                    <form>
                        <div className="field">
                            <div className="control">
                                <input className="input" type="email" placeholder="Your Email" autofocus=""></input>
                            </div>
                        </div>

                        <div className="field">
                            <div className="control">
                                <input className="input" type="password" placeholder="Your Password"></input>
                            </div>
                        </div>
                        <div className="field">
                        </div>
                        <button className="button is-block is-info is-medium is-fullwidth">Login <i className="fa fa-sign-in" aria-hidden="true"></i></button>
                    </form>
                </div>
                <p className="has-text-grey">
                    <a href="../">Sign Up</a> &nbsp;·&nbsp;
                </p>
            </div>
            </div>
        </>
    );
}

export default LoginForm