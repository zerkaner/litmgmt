import * as React from "react";
import { Route, Switch } from 'react-router-dom';

import LoginPage from "./pages/login";
import CollectionsPage from "./pages/collections";
import { PrivateRoute } from "./router/PrivateRoute";

function App() {

    return (
        <Switch>
            <PrivateRoute exact path="/" component={CollectionsPage} />
            <PrivateRoute path="/collections/:id" component={CollectionsPage} />
            <Route path="/login" component={LoginPage} />
        </Switch>
    );
}

export default App;