import React, {useState} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil } from '@fortawesome/free-solid-svg-icons'
import TopicField from './topic-field/topic-field';
import DescriptionField from './description-field/description-field';
import RecordGrid from './record-grid/record-grid';
import RemoveButton from '../util/components/remove-button';
import useSimpleState from '../util/hooks/useSimpleState';

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
                id:props.data.id,
                topic:props.data.topic,
                description:editDescription,
                recordList:editRecordList
            });
            setMode("VIEW");
        }
    }

    const handleReset = e => {
        e.preventDefault();
        props.resetJournalBodyForm();
    }

    const remove = (id) => {
        props.removeFromUsedTopics(undefined,props.data.topic);
        props.removeFromBody(id);
    }

    const handleCancelEdit = e => {
        e.preventDefault();
        setEditDescription(props.data.description);
        setEditRecordList([...props.data.recordList]);
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
                            <RemoveButton id={props.data.id} remove={remove} className="btn btn-secondary float-end"></RemoveButton>
                            <button id={"edit_"+props.data.id} className="btn btn-secondary float-end" onClick={handleEdit}><FontAwesomeIcon icon={faPencil} /></button>
                        </div>
                    :
                    null
                }    
                
            </div>
            
            <div className="card-body">
                <DescriptionField 
                    description={editDescription} 
                    handleChangeDescription={handleEditDescription} 
                    mode={mode}></DescriptionField>

                <br />
                <RecordGrid 
                    recordList={(mode==="EDIT")? editRecordList : props.data.recordList} 
                    setRecordList={(mode==="EDIT")? setEditRecordList : props.setRecordList}
                    mode={mode}></RecordGrid>

                    <div className="mb-3 row">
                        {
                            (mode==="NEW")?
                                <div className="col">
                                    <button id="clearBodyFormBtn" className="btn btn-outline-secondary" onClick={handleReset}>Clear</button> 
                                </div>
                            :
                                null
                        }

                        {
                            (mode==="EDIT")?
                                <div className="col">
                                    <button id="cancelEditBodyFormBtn" className="btn btn-outline-secondary" onClick={handleCancelEdit}>Cancel</button> 
                                </div>
                            :
                                null
                        }


                        {
                            (mode==="VIEW")?
                                null 
                            :
                                <div className="col">
                                    <button id="addToBodyBtn" className="btn btn-primary float-end" onClick={handleSubmit}>Save</button> 
                                </div> 
                        }                 
                    </div>
            </div>  
        </div>      
    );

}

export default JournalBodyItem;