import React from 'react';

function SimpleInput(props){
    return(
        <div>
            <div className="mb-3">
                {
                    (props.displayName !== undefined )?
                        <label htmlFor={props.fieldName} className="form-label">{props.displayName}</label> : null
                }
                
                {
                    (props.type === "textarea") ?
                        (<textarea id={props.id} type="text" className="form-control" name={props.fieldName} value={props.value} onChange={props.handleUpdate}></textarea>) :
                    (props.type === "select") ?
                        (
                            <select id={props.id} className="form-select" name={props.fieldName} defaultValue={props.value} onChange={props.handleUpdate}>
                                {
                                    props.optionList.map( (optionListItem) => (
                                        <option value={optionListItem}>{optionListItem}</option>
                                    ))
                                }
                            </select>
                        ) : 
                        (<input id={props.fieldName} type={props.type} className="form-control" name={props.fieldName} value={props.value} onChange={props.handleUpdate} placeholder={props.placeholder}></input>)
                }
            </div>
            <br/>
        </div>

    );



}

export default SimpleInput;