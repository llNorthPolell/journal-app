import React, {useState} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPlus } from '@fortawesome/free-solid-svg-icons'
import RecordForm from './record-form/record-form';
import {v4 as uuidv4} from 'uuid';
import useSimpleState from '../util/hooks/useSimpleState';
import SimpleInput from '../util/components/simple-input';

function JournalBodyItemForm(props){
    const [topicList, setTopicList]= useState(props.topicList);
    const [topic, setTopic] = useState("");
    const [newTopic, setNewTopic] = useState("");
    const [recordList, setRecordList] = useState([]);

    const [description,handleChangeDescription,setDescription] = useSimpleState("");

    const handleChangeTopic = e =>{
        setTopic(e.target.value);
    }
    
    const handleChangeNewTopic = e =>{
        setNewTopic(e.target.value);
    }

    const resetAll = e =>{
        e.preventDefault();
        setTopic("");
        setDescription("");
        setNewTopic("");
        setRecordList([]);
    }

    const handleSubmit = e => {
        e.preventDefault();

        let topicToSubmit = "";

        if (topic=="" && newTopic==""){
            console.log("Cannot submit blank Topic...")
            return;
        }
        else if (topic=="" && newTopic!="") {
            if (topicIsUsed(newTopic))
                return;

            topicToSubmit = newTopic;

            if (!topicInTopicList(newTopic))
                setTopicList([...topicList,newTopic]);
        }
        else {
            if (topicIsUsed(topic))
                return;
            
            topicToSubmit = topic;
        }
            
        props.addToBody(topicToSubmit,description,recordList);
        props.addToUsedTopics(topicToSubmit);
        resetAll(e);
                         
    }

    const topicIsUsed = checkTopic => {
        let output = props.usedTopics.some(usedTopic=> usedTopic == checkTopic);
        console.log("Topic is already used...");
        return output;
    }

    const topicInTopicList = checkTopic => {
        let output = topicList.some(topic=> topic == checkTopic);
        return output;
    }

    const insertNewRecord = e => {
        e.preventDefault();
        setRecordList([...recordList, {id:uuidv4(), key:"", value:""}])
    }

    const updateRecord = (id,newKey,newValue) =>{
        const updatedRecordList = recordList.map(record=>
            record.id === id ? {...record, key: newKey, value: newValue} : record   
        );
        setRecordList(updatedRecordList);
    }

    const removeRecord = (id) => {
        const updatedRecordList = recordList.filter(record => record.id!==id);
        setRecordList(updatedRecordList);
    }

    return(   
        <div className="card">
            <div className="card-header">
                <div className="row mb-3">
                    <div className="col">
                        <select id="topicDropdown" className="form-select" name="topic" value={topic} onChange={handleChangeTopic}>
                            <option value="">New Topic</option>
                            {
                                topicList.map(topicListItem => (
                                    <option value={topicListItem}>{topicListItem}</option>
                                ))
                            }
                        </select>
                    </div>
                    <div className="col">
                        <input id="newTopicField" className="form-control" name="newTopic" value={newTopic} onChange={handleChangeNewTopic} placeholder="New Topic Name"/>
                    </div>
                </div>
            </div>
            
            <div className="card-body">
                <SimpleInput 
                    id="descriptionField" 
                    value={description} 
                    displayName = "Description"
                    fieldName="description" 
                    type="textarea" 
                    handleUpdate={handleChangeDescription}></SimpleInput>  

                <div className="row">
                    <div className="col">
                        <label htmlFor="recordsDiv" className="form-label">Records</label>
                    </div>
                    <div className="col col-md-4">
                        <button id="newRecordBtn" className="btn link-primary" onClick={insertNewRecord}><FontAwesomeIcon icon={faPlus} /></button>
                    </div>
                </div>
                
                <br/>
                
                <div id="recordsDiv">
                {
                    recordList.map(
                        record => (
                            <RecordForm id={record.id} key={record.id} recordKey={record.key} recordValue={record.value} updateRecord={updateRecord} removeRecord={removeRecord}></RecordForm>
                        )
                    )
                }
                </div>
                <div className="mb-3 row">
                    <div className="col">
                        <button id="clearBodyFormBtn" className="btn btn-outline-secondary" onClick={resetAll}>Clear</button> 
                    </div>
                    <div className="col">
                        <button id="addToBodyBtn" className="btn btn-primary float-end" onClick={handleSubmit}>Save</button> 
                    </div>                   
                </div>
            </div>  
        </div>      
    );
}

export default JournalBodyItemForm;