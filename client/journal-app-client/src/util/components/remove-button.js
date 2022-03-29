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
            <button id={"delete_"+props.id} className={props.className} onClick={handleDelete}>
                {
                    (props.inner===undefined)?
                        <FontAwesomeIcon icon={faTrashCan} /> :
                        props.inner
                }
                        
            </button>
        </div>
    );


}
export default RemoveButton;