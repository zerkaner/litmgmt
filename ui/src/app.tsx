import * as React from "react";
import { Route, Switch } from 'react-router-dom';

import LoginPage from "./pages/login";
import CollectionsPage from "./pages/collections";

function App() {

    return (
        <Switch>
            <Route exact path="/" component={CollectionsPage} />
            <Route path="/collections/:id" component={CollectionsPage} />
            <Route path="/login" component={LoginPage} />
        </Switch>
    );
}

export default App;