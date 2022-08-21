import React, {useState} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil } from '@fortawesome/free-solid-svg-icons'
import TopicField from './topic-field/topic-field';
import DescriptionField from './description-field/description-field';
import RecordGrid from './record-grid/record-grid';
import RemoveButton from '../util/components/remove-button';
import useSimpleState from '../util/hooks/useSimpleState';
import listUtil from '../util/functions/list-util';

function JournalBodyItem(props){
    const [editDescription,setEditDescription,handleEditDescription] = useSimpleState(props.data.description);
    const [editRecordList,setEditRecordList]=useState(props.data.recordList);

    const [mode, setMode] = useState(props.mode);

    const handleSubmit = e => {
        e.preventDefault();

        if (mode==="NEW")
            props.saveJournalBodyItem();
        else if (mode==="EDIT"){
            props.updateJournalBodyItem({
                ...props.data,
                description:editDescription,
                recordList:editRecordList
            });
            setMode("VIEW");
        }
    }

    const handleClear = e => {
        e.preventDefault();
        props.clearJournalBodyForm();
    }

    const handleRemove = e => {
        props.removeFromUsedTopics(undefined,props.data.topic);
        props.removeFromBody(props.data.key);
    }

    const handleCancelEdit = e => {
        e.preventDefault();
        setEditDescription(props.data.description);
        listUtil(editRecordList,setEditRecordList,{type:"SET", payload:props.data.recordList});
        setMode("VIEW");
    }

    const handleEdit = e => {
        e.preventDefault();
        setMode("EDIT");
    }

    return(   
        <div className="card">
            <div className="card-header">
                <TopicField 
                    topic={props.data.topic} 
                    newTopic={props.newTopic}
                    handleChangeTopic={props.handleChangeTopic} 
                    topicList={props.topicList}
                    handleChangeNewTopic={props.handleChangeNewTopic}
                    mode={mode}></TopicField>

                {
                    (mode==="VIEW")?
                        <div className="col">
                            <RemoveButton id={props.data.key} remove={handleRemove} className="btn btn-secondary float-end"></RemoveButton>
                            <button id={"edit_"+props.data.key} className="btn btn-secondary float-end" onClick={handleEdit}><FontAwesomeIcon icon={faPencil} /></button>
                        </div>
                    :
                    null
                }    
                
            </div>
            
            <div className="card-body">
                <DescriptionField 
                    description={(mode!=="NEW")? editDescription: props.data.description} 
                    handleChangeDescription={(mode!=="NEW")?handleEditDescription :props.handleChangeDescription} 
                    mode={mode}></DescriptionField>

                <br />
                <RecordGrid 
                    recordList={(mode!=="NEW")?editRecordList:props.data.recordList} 
                    setRecordList={(mode!=="NEW")? setEditRecordList : props.setRecordList}
                    mode={mode}></RecordGrid>

                    <div className="mb-3 row">
                        {
                            (mode==="NEW")?
                                <>
                                    <div className="col">
                                        <button id="clearBodyFormBtn" className="btn btn-outline-secondary" onClick={handleClear}>Clear</button> 
                                    </div>
                                    <div className="col">
                                        <button id="addToBodyBtn" className="btn btn-primary float-end" onClick={handleSubmit}>Add to Body</button> 
                                    </div> 
                                </>

                            :
                                null
                        }

                        {
                            (mode==="EDIT")?
                                <>
                                    <div className="col">
                                        <button id="cancelEditBodyFormBtn" className="btn btn-outline-secondary" onClick={handleCancelEdit}>Cancel</button> 
                                    </div>
                                    <div className="col">
                                        <button id="addToBodyBtn" className="btn btn-primary float-end" onClick={handleSubmit}>Save</button> 
                                    </div> 
                                </>
                            :
                                null
                        }

                    </div>
            </div>  
        </div>      
    );

}

export default JournalBodyItem;