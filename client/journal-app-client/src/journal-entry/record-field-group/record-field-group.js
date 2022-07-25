import React,{useEffect, useState} from 'react';
import RemoveButton from '../../util/components/remove-button'
import listUtil from '../../util/functions/list-util';

function RecordFieldGroup(props){
    const [recordKey,setRecordKey] = useState(props.data.key);
    const [recordValue,setRecordValue] = useState(props.data.value);

    useEffect(()=>{
        setRecordKey(props.data.key);
    },[props.data.key])

    useEffect(()=>{
        setRecordValue(props.data.value);
    },[props.data.value])

    const handleChangeRecordKey = e => { 
        listUtil(props.recordList,props.setRecordList,{type:"UPDATE",payload:{id:props.data.id,key:e.target.value, value:props.data.value}});
    }

    const handleChangeRecordValue = e => {
        listUtil(props.recordList,props.setRecordList,{type:"UPDATE",payload:{id:props.data.id,key:props.data.key, value:e.target.value}});
    }

    return(
        <div id={'record_'+props.data.id} className="row mb-3">
            <div className="col">
                <input 
                    id={"recordKeyField_"+props.data.id} 
                    type="text" 
                    className="form-control" 
                    name="recordKey" 
                    value={recordKey} 
                    onChange={handleChangeRecordKey} 
                    placeholder="Key"
                    disabled={props.mode==="VIEW"}/>
            </div>
            <div className="col">
                <input 
                    id={"recordValueField_"+props.data.id} 
                    type="text" className="form-control" 
                    name="recordValue" 
                    value={recordValue} 
                    onChange={handleChangeRecordValue} 
                    placeholder="Value"
                    disabled={props.mode==="VIEW"}/>
            </div>
            {
                (props.mode==="VIEW")?
                    null
                :
                    <RemoveButton id={props.data.id} className="btn btn-secondary" remove={()=>listUtil(props.recordList,props.setRecordList,{type:"DELETE",id:props.data.id})}></RemoveButton> 
            }
            
        </div>
    );


  
}

export default RecordFieldGroup;