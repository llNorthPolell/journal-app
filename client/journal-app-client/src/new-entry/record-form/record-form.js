import React from 'react';
import RemoveButton from '../../util/components/remove-button'
import listUtil from '../../util/functions/list-util';

function RecordForm(props){
    
    const handleChangeRecordKey = e => {
        listUtil(props.recordList,props.setRecordList,{type:"UPDATE",payload:{id:props.data.id,key:e.target.value, value:props.data.value}});
    }

    const handleChangeRecordValue = e => {
        listUtil(props.recordList,props.setRecordList,{type:"UPDATE",payload:{id:props.data.id,key:props.data.key, value:e.target.value}});
    }

    return(
        <div id={'recordform_'+props.data.id} className="row mb-3">
            <div className="col">
                <input id={"recordKeyField_"+props.data.id} type="text" className="form-control" name="recordKey" defaultValue={props.data.key} onBlur={handleChangeRecordKey} placeholder="Key"/>
            </div>
            <div className="col">
                <input id={"recordValueField_"+props.data.id} type="text" className="form-control" name="recordValue" defaultValue={props.data.value} onBlur={handleChangeRecordValue} placeholder="Value"/>
            </div>
            <RemoveButton id={props.data.id} className="btn btn-secondary" remove={()=>listUtil(props.recordList,props.setRecordList,{type:"DELETE",id:props.data.id})}></RemoveButton>
        </div>
    );


  
}

export default RecordForm;