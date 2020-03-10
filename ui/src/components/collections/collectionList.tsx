import React from 'react';
import CollectionListItem from './collectionListItem';

function CollectionList() {

    return (
        <div class="card">
            <div class="card-table">
                <div class="content">
                    <table class="table is-fullwidth">
                        <tbody>
                            <CollectionListItem itemName="Adbis Paper" />
                            <CollectionListItem itemName="IEEE Chicago" />
                            <CollectionListItem itemName="ACM Hamburg" />
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
}

export default CollectionList;