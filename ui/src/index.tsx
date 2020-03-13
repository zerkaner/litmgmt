import * as React from 'react';
import * as ReactDOM from 'react-dom'
import { BrowserRouter } from 'react-router-dom'
import App from "./app";
import { AuthProvider } from './contexts/authContext';

ReactDOM.render(
    <AuthProvider>
        <BrowserRouter>
            <App />
        </BrowserRouter>
    </AuthProvider>,
    document.getElementById('root')
);