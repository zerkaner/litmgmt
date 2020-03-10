import React from 'react';
import Menue from '../components/header/header';
import Footer from '../components/footer/footer';

function CollectionsPage() {
    return (
        <>
           <Menue title="Litmgmt - Bibtex Manager" />
            <div className="columns body-columns">
                <div className="column is-half is-offset-one-quarter">
                    Collections
                </div>
            </div>
            <Footer />
        </>
    );
}

export default CollectionsPage;