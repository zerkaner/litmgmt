import * as React from "react";
import CollectionListItem from './collectionListItem';

function CollectionList() {

    return (
        <div className="card">
            <div className="card-table">
                <table className="table is-fullwidth">
                    <tbody>
                        <CollectionListItem itemName="Adbis Paper" />
                        <CollectionListItem itemName="IEEE Chicago" />
                        <CollectionListItem itemName="ACM Hamburg" />
                    </tbody>
                </table>
            </div>
        </div>
    );
}

export default CollectionList;