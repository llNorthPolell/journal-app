import React from 'react';

function SimpleInput(props){
    return(
        <div>
            <div className="mb-3">
                {
                    (props.displayName)?
                        <label htmlFor={props.id} className="form-label">{props.displayName}</label> : null
                }
                
                {
                    (props.type === "textarea") ?
                        (<textarea id={props.id} type="text" className={"form-control "+props.className} name={props.fieldName} value={props.value} onChange={props.handleUpdate} placeholder={props.placeholder}></textarea>) :
                    (props.type === "select") ?
                        (
                            <select id={props.id} className={"form-select "+props.className} name={props.fieldName} defaultValue={props.value} onChange={props.handleUpdate}> 
                                <option value="">{props.defaultOptionDisplay}</option>
                                {    
                                    props.optionList.map( (optionListItem) => (
                                        <option value={optionListItem}>{optionListItem}</option>
                                    ))
                                }
                            </select>
                        ) : 
                        (<input id={props.id} type={props.type} className={"form-control "+props.className} name={props.fieldName} value={props.value} onChange={props.handleUpdate} placeholder={props.placeholder}></input>)
                }
            </div>
            <br/>
        </div>

    );



}

export default SimpleInput;