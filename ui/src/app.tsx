import * as React from 'react';

export class App extends React.Component {

    constructor(props) {
        super(props);
    };

    render() {
        return (
            <div class="navbar is-inline-flex is-transparent">
        <div class="navbar-brand">
            <a class="navbar-item">
                <img src="https://bulma.io/images/bulma-logo.png" width="112" height="28" alt="Bulma">
            </a>
        </div>
        <div class="navbar-menu">
            <div class="navbar-item">
                <div class="control has-icons-left">
                    <input class="input is-small has-text-centered" type="text" placeholder="search">
                    <span class="icon is-left is-small">
                        <i class="material-icons">search</i>
                    </span>
                </div>
            </div>
        </div>
        <div class="navbar-item is-flex-touch">
            <a class="navbar-item">
                <i class="material-icons">explore</i>
            </a>
            <a class="navbar-item">
                <i class="material-icons">favorite_border</i>
            </a>
            <a class="navbar-item">
                <i class="material-icons">person_outline</i>
            </a>
        </div>
    </div>
    <div class="columns body-columns">
        <div class="column is-half is-offset-one-quarter">
            <div class="card">
                <div class="header">
                    <div class="media">
                        <div class="media-left">
                            <figure class="image is-48x48">
                                <img src="https://source.unsplash.com/random/96x96" alt="Placeholder image">
                            </figure>
                        </div>
                        <div class="media-content">
                            <p class="title is-4">John Smith</p>
                            <p class="subtitle is-6">@johnsmith</p>
                        </div>
                    </div>
                </div>
                <div class="card-image">
                    <figure class="image is-4by3">
                        <img src="https://source.unsplash.com/random/1280x960" alt="Placeholder image">
                    </figure>
                </div>
                <div class="card-content">
                    <div class="level is-mobile">
                        <div class="level-left">
                            <div class="level-item has-text-centered">
                                <a href="">
                                    <i class="material-icons">favorite_border</i>
                                </a>
                            </div>
                            <div class="level-item has-text-centered">
                                <div>
                                    <a href="">
                                        <i class="material-icons">chat_bubble_outline</i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="content">
                        <p>
                            <strong>32 Likes</strong>
                        </p>
                        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Phasellus nec iaculis mauris.
                        <a>@bulmaio</a>.
                        <a href="#">#css</a>
                        <a href="#">#responsive</a>
                        <br>
                        <time datetime="2018-1-1">11:09 PM - 1 Jan 2018</time>
                    </div>
                </div>
                <div class="card-footer">
                    <div class="columns is-mobile">
                        <div class="column is-11">
                            <div class="field">
                                <div class="control">
                                    <input class="input is-medium" type="text" placeholder="Add a comment . . .">
                                </div>
                            </div>
                        </div>
                        <div class="column has-text-centered">
                            <button class="button">
                                <i class="material-icons">more_horiz</i>
                            </button>
                        </div>
                    </div>
                </div>


            <footer class="footer">
                <div class="container is-fluid">
                    <div class="content has-text-centered">
                        <p>
                            <strong>Bulma</strong> by
                            <a href="http://jgthms.com">Jeremy Thomas</a>. The source code is licensed
                            <a href="http://opensource.org/licenses/mit-license.php">MIT</a>. The website content is licensed
                            <a href="http://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY NC SA 4.0</a>.
                        </p>
                    </div>
                </div>
            </footer>

        </div>
    </div>

        )
    }
}