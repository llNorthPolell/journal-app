import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faTrashCan } from '@fortawesome/free-solid-svg-icons'

function RemoveButton(props){
    const handleDelete = e =>{
        e.preventDefault();
        props.remove(props.id);
    }

    return (
        <div className="col">
            <button id={"delete_"+props.id} className={"btn btn-secondary "+props.moreClasses} onClick={handleDelete}><FontAwesomeIcon icon={faTrashCan} /></button>
        </div>
    );


}
export default RemoveButton;