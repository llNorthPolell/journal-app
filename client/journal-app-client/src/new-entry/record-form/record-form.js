import React from 'react';
import RemoveButton from '../../util/components/remove-button'


function RecordForm(props){
    
    const handleChangeRecordKey = e => {
        props.updateRecord(props.id,e.target.value, props.recordValue);
    }

    const handleChangeRecordValue = e => {
        props.updateRecord(props.id,props.recordKey, e.target.value);
    }

    return(
        <div id={'recordform_'+props.id} className="row mb-3">
            <div className="col">
                <input id={"recordKeyField_"+props.id} type="text" className="form-control" name="recordKey" defaultValue={props.recordKey} onBlur={handleChangeRecordKey} placeholder="Key"/>
            </div>
            <div className="col">
                <input id={"recordValueField_"+props.id} type="text" className="form-control" name="recordValue" defaultValue={props.recordValue} onBlur={handleChangeRecordValue} placeholder="Value"/>
            </div>
            <RemoveButton id={props.id} remove={props.removeRecord}></RemoveButton>
        </div>
    );


  
}

export default RecordForm;