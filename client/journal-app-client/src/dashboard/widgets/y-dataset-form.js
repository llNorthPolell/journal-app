import React, {useState,useEffect} from 'react';
import RemoveButton from '../../util/components/remove-button';
import listUtil from '../../util/functions/list-util';
import {useDashboard} from '../../contexts/dashboardContext';

function YDatasetForm (props){
    const [yColor,setYColor] = useState("#000");
    const [yDatasetName,setYDatasetName] = useState("");
    const [yTopic,setYTopic] = useState("");
    const [yTopicList,setYTopicList] = useState([]);
    const [yRecord,setYRecord] = useState("");
    const [yRecordList,setYRecordList]= useState([]);

    const {currentJournal} = useDashboard();

    useEffect(()=>{
        if (currentJournal){
            const yTopicList = currentJournal.schemas.map(schema=>schema.topic);
            const yTopic = (yTopicList.length>0)?yTopicList[0]:"";
            const schema = currentJournal.schemas.find(schema=>schema.topic===yTopic);
            const yRecord =  (schema.records.length>0)?schema.records[0]:"";


            setYTopicList(yTopicList);
            setYTopic(yTopic);
            setYRecordList(schema.records);
            setYRecord(yRecord);
        }
        else setYTopicList([]);
    },[currentJournal])

    useEffect(()=>{
        if (yTopic){
            const schema = currentJournal.schemas.find(schema=>schema.topic===yTopic)
            setYRecordList(schema.records);
        } 
        else setYRecordList([]);
    },[yTopic])

    const handleChangeYColor = e => {
        setYColor(e.target.value);
        listUtil(
            props.yList, 
            props.setYList, 
            {
                type:"UPDATE", 
                payload:{
                    id: props.id,
                    backgroundColor: e.target.value, 
                    borderColor: e.target.value,
                    label: yDatasetName,
                    topic: yTopic,
                    record: yRecord
                }
            }
        );
    }

    const handleChangeYDatasetName = e => {
        setYDatasetName(e.target.value);
        listUtil(
            props.yList, 
            props.setYList, 
            {
                type:"UPDATE", 
                payload:{
                    id: props.id,
                    backgroundColor: yColor, 
                    borderColor: yColor,
                    label: e.target.value,
                    topic: yTopic,
                    record: yRecord
                }
            }
        );
    }
    
    const handleChangeYTopic = e => {
        setYTopic(e.target.value);
        listUtil(
            props.yList, 
            props.setYList, 
            {
                type:"UPDATE", 
                payload:{
                    id: props.id,
                    backgroundColor: yColor, 
                    borderColor: yColor,
                    label: yDatasetName,
                    topic: e.target.value,
                    record: yRecord
                }
            }
        );
    }

    const handleChangeYRecord = e => {
        setYRecord(e.target.value);
        listUtil(
            props.yList, 
            props.setYList, 
            {
                type:"UPDATE", 
                payload:{
                    id: props.id,
                    backgroundColor: yColor, 
                    borderColor: yColor,
                    label: yDatasetName,
                    topic: yTopic,
                    record: e.target.value
                }
            }
        );
    }

    
    return (
        <fieldset className="lineGraphYForm">
            <div className="mb-3">
                <RemoveButton id={props.key} className="btn btn-secondary" remove={()=>listUtil(props.yList,props.setYList,{type:"DELETE",id:props.id})}></RemoveButton> 
            </div>
            <div className="mb-3">
                <label htmlFor="yColorField">Color</label>
                <input id="yColorField" type="color" className="form-control" name="yColor" value={yColor} onChange={handleChangeYColor} placeholder="#000"></input>
            </div>
            <div className="mb-3">
                <label htmlFor="yDatasetNameField">Dataset Name</label>
                <input id="yDatasetNameField" type="text" className="form-control" name="yDatasetName" value={yDatasetName} onChange={handleChangeYDatasetName} placeholder="Dataset Name"></input>
            </div>
            <div className="row">
                <div className="mb-3 col">
                    <label htmlFor="yTopicField">Topic</label>
                    <select id="yTopicField" className="form-select" name="yTopic" value={yTopic} onChange={handleChangeYTopic} placeholder="Topic">
                        {
                            yTopicList.map(yTopic=>
                                <option value={yTopic}>{yTopic}</option>
                            )
                        }
                    </select>
                </div>
                <div className="mb-3 col">
                    <label htmlFor="yRecordField">Record Name</label>
                    <select id="yRecordField" className="form-select" name="yRecord" value={yRecord} onChange={handleChangeYRecord} placeholder="Record">
                        {
                            yRecordList.map(yRecord=>
                                <option value={yRecord}>{yRecord}</option>
                            )
                        }

                    </select>
                </div>
            </div>
        </fieldset>
    )


}
export default YDatasetForm