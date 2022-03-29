import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPlus } from '@fortawesome/free-solid-svg-icons'
import RecordForm from './record-form/record-form';
import {v4 as uuidv4} from 'uuid';
import SimpleInput from '../util/components/simple-input';


function JournalBodyItemForm(props){
    const handleInsertNewRecord = e => {
        e.preventDefault();
        props.insertNewRecord({id:uuidv4(), key:"", value:""});
    }

    const handleSubmit = e => {
        e.preventDefault();
        props.saveJournalBodyItem();
    }

    const handleReset = e => {
        e.preventDefault();
        props.resetJournalBodyForm();
    }

    return(   
        <div className="card">
            <div className="card-header">
                <div className="row mb-3">
                    <div className="col">
                        <select id="topicDropdown" className="form-select" name="topic" value={props.topic} onChange={props.handleChangeTopic}>
                            <option value="">New Topic</option>
                            {
                                props.topicList.map(topicListItem => (
                                    <option value={topicListItem}>{topicListItem}</option>
                                ))
                            }
                        </select>
                    </div>
                    <div className="col">
                        {
                            (props.topic=="")?
                                <input id="newTopicField" className="form-control" name="newTopic" value={props.newTopic} onChange={props.handleChangeNewTopic} placeholder="New Topic Name"/>
                                :
                                null
                        }
                    </div>
                </div>
            </div>
            
            <div className="card-body">
                <SimpleInput 
                    id="descriptionField" 
                    value={props.description} 
                    displayName = "Description"
                    fieldName="description" 
                    type="textarea" 
                    handleUpdate={props.handleChangeDescription}></SimpleInput>  

                <div className="row">
                    <div className="col">
                        <label htmlFor="recordsDiv" className="form-label">Records</label>
                    </div>
                    <div className="col col-md-4">
                        <button id="newRecordBtn" className="btn link-primary" onClick={handleInsertNewRecord}><FontAwesomeIcon icon={faPlus} /></button>
                    </div>
                </div>
                
                <br/>
                
                <div id="recordsDiv">
                {
                    props.recordList.map(
                        record => (
                            <RecordForm id={record.id} key={record.id} recordKey={record.key} recordValue={record.value} updateRecord={props.updateRecord} removeRecord={props.removeRecord}></RecordForm>
                        )
                    )
                }
                </div>
                <div className="mb-3 row">
                    <div className="col">
                        <button id="clearBodyFormBtn" className="btn btn-outline-secondary" onClick={handleReset}>Clear</button> 
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