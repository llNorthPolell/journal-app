import React from 'react';
import RemoveButton from '../../util/components/remove-button'


function RecordForm(props){
    
    const handleChangeRecordKey = e => {
        props.updateRecord({id:props.id,key:e.target.value, value:props.recordValue});
    }

    const handleChangeRecordValue = e => {
        props.updateRecord({id:props.id,key:props.recordKey, value:e.target.value});
    }

    return(
        <div id={'recordform_'+props.id} className="row mb-3">
            <div className="col">
                <input id={"recordKeyField_"+props.id} type="text" className="form-control" name="recordKey" defaultValue={props.recordKey} onBlur={handleChangeRecordKey} placeholder="Key"/>
            </div>
            <div className="col">
                <input id={"recordValueField_"+props.id} type="text" className="form-control" name="recordValue" defaultValue={props.recordValue} onBlur={handleChangeRecordValue} placeholder="Value"/>
            </div>
            <RemoveButton id={props.id} className="btn btn-secondary" remove={props.removeRecord}></RemoveButton>
        </div>
    );


  
}

export default RecordForm;