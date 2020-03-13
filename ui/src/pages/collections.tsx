import * as React from "react";
import Menue from '../components/header/header';
import Footer from '../components/footer/footer';
import CollectionList from '../components/collections/collectionList';
import { useAuthState, useAuthDispatch } from "../contexts/authContext";

function CollectionsPage() {

    const auth = useAuthState();
    const dispatch = useAuthDispatch();

    return (
        <>
           <Menue title="Litmgmt - Bibtex Manager" />
            <div className="columns body-columns">
                <div className="column is-half is-offset-one-quarter">
                    {auth.username}

                    <button onClick={() => dispatch({type: 'setContext', username: "foo"})}>Press me</button>
                    <CollectionList />
                </div>
            </div>
            <Footer />
        </>
    );
}

export default CollectionsPage;