import * as React from "react";

function CollectionListItem(props: any) {

    return (
        <tr>
            <td width="1%"></td>
            <td>{props.itemName}</td>
            <td className="level-right">
                <a className="button is-small is-info" href="#">Edit</a>
            </td>
        </tr>
    );
}

export default CollectionListItem