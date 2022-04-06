import React, {useState} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil } from '@fortawesome/free-solid-svg-icons'
import TopicField from './topic-field/topic-field';
import DescriptionField from './description-field/description-field';
import RecordGrid from './record-grid/record-grid';
import RemoveButton from '../util/components/remove-button';
import useSimpleState from '../util/hooks/useSimpleState';

function JournalBodyItem(props){
    const [editDescription,setEditDescription,handleEditDescription] = useSimpleState("");
    const [editRecordList,setEditRecordList]=useState([]);

    const handleSubmit = e => {
        e.preventDefault();

        if (props.mode=="NEW")
            props.saveJournalBodyItem();
        else if (props.mode=="EDIT"){
            props.updateJournalBodyItem({
                id:props.data.id,
                topic:props.data.topic,
                description:editDescription,
                recordList:editRecordList
            });
            props.releaseEdit();
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
        setEditRecordList([...props.data.recordList]);
        props.releaseEdit();
    }

    const handleEdit = e => {
        e.preventDefault();
        setEditDescription(props.data.description);
        setEditRecordList([...props.data.recordList]);
        props.takeEdit(props.data.id);
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
                    mode={props.mode}></TopicField>

                {
                    (props.mode=="VIEW")?
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
                    description={(props.mode=="EDIT")? editDescription : props.data.description} 
                    handleChangeDescription={(props.mode=="EDIT")? handleEditDescription : props.handleChangeDescription} 
                    mode={props.mode}></DescriptionField>


                <RecordGrid 
                    recordList={(props.mode=="EDIT")? editRecordList : props.data.recordList} 
                    setRecordList={(props.mode=="EDIT")? setEditRecordList : props.setRecordList}
                    mode={props.mode}></RecordGrid>

                    <div className="mb-3 row">
                        {
                            (props.mode=="NEW")?
                                <div className="col">
                                    <button id="clearBodyFormBtn" className="btn btn-outline-secondary" onClick={handleReset}>Clear</button> 
                                </div>
                            :
                                null
                        }

                        {
                            (props.mode=="EDIT")?
                                <div className="col">
                                    <button id="cancelEditBodyFormBtn" className="btn btn-outline-secondary" onClick={handleCancelEdit}>Cancel</button> 
                                </div>
                            :
                                null
                        }


                        {
                            (props.mode=="VIEW")?
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