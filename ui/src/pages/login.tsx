import * as React from "react";
import Menue from '../components/header/header';
import Footer from '../components/footer/footer';
import LoginForm from '../components/loginForm/loginForm';
import { useAuthDispatch, useAuthState } from "../contexts/authContext";

function LoginPage() {
    return (
        <>
            <Menue title="Litmgmt - Bibtex Manager" />
            <div className="columns body-columns">
                <div className="column is-half is-offset-one-quarter">
                    <LoginForm />
                </div>
            </div>
            <Footer />
        </>
    );
}

export default LoginPage