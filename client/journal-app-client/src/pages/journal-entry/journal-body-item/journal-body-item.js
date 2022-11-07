import React, {useState} from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPencil,faPlus } from '@fortawesome/free-solid-svg-icons'
import RemoveButton from '../../../util/components/remove-button';
import useSimpleChildForm from '../../../util/hooks/useSimpleChildForm';
import SimpleInput from '../../../util/components/simple-input';
import { DefaultRecord } from './record-form/record-dto';
import RecordForm from './record-form/record-form';
import { v4 as uuidv4 } from 'uuid';

function JournalBodyItemForm(props){
    const [formFields,updateForm,saveJournalBodyItem,resetForm] = useSimpleChildForm(props.data,props.save);
    const [mode, setMode] = useState(props.mode);

    const handleSubmit = e => {
        e.preventDefault();
    
        const error = saveJournalBodyItem(e);
        
        if (error)return;

        if (mode==="EDIT")
            setMode("VIEW");
        else if (mode==="NEW")
            resetForm();
    }

    const handleChange = e => {
        updateForm({[e.target.name]: e.target.value});
    }

    const handleClear = e => {
        e.preventDefault();
        resetForm();
    }

    
    const handleCancelEdit = e => {
        e.preventDefault();
        resetForm();
        setMode("VIEW");
    }

    const handleEdit = e => {
        e.preventDefault();
        setMode("EDIT");
    }

    const handleInsertNewRecord = e => {
        e.preventDefault();
        updateForm({recordList:[...formFields.recordList,{...DefaultRecord, id:uuidv4()}]});
    }

    const removeRecord = id => {
        updateForm({recordList:formFields.recordList.filter(record=> record.id!==id)});
    }

    const updateRecord = (e,id) => {
        e.preventDefault();
        let newRecordList = formFields.recordList.map(record =>
            record.id === id ? ({ ...record, [e.target.name]: e.target.value.trim()}) : record);

        updateForm({ recordList: newRecordList });
    }

    return (
        <div className="card">
            <div className="card-header">
                <div className="row">
                    {
                        (mode === "NEW") ?
                            <>
                                <div className="col">
                                    <select id="topicDropdown" className="form-select" name="topic" value={formFields.topic} onChange={handleChange}>
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
                                        (formFields.topic == "") ?

                                            <SimpleInput
                                                id="newTopicField"
                                                fieldName="newTopic"
                                                type="text"
                                                placeholder="New Topic Name"
                                                value={formFields.newTopic}
                                                handleUpdate={handleChange} />

                                            :
                                            <></>

                                    }
                                </div>
                            </>

                            :
                            <div className="col">
                                <h5 className="card-title">{props.data.topic}</h5>
                            </div>
                    }

                    {
                        (mode === "VIEW") ?
                            <>
                                <div className="col">
                                    <RemoveButton id={props.data.id} remove={props.remove} className="btn btn-secondary float-end" />
                                    <button id={"edit_" + props.data.id} className="btn btn-secondary float-end" onClick={handleEdit}><FontAwesomeIcon icon={faPencil} /></button>
                                </div>
                            </>
                            :
                            <></>
                    }
                </div>
            </div>

            <div className="card-body">
                {
                    (mode === "VIEW") ?
                        <p className="card-text">{formFields.description}</p>
                        :
                        <SimpleInput
                            id="descriptionField"
                            fieldName="description"
                            displayName="Description"
                            type="textarea"
                            value={formFields.description}
                            handleUpdate={handleChange}></SimpleInput>
                }


                <div className="row">
                    <div className="col">
                        <label htmlFor="recordsDiv" className="form-label underline">Records</label>
                    </div>
                    {
                        (mode === "VIEW") ?
                            <></>
                            :
                            <div className="col col-md-4">
                                <button id="newRecordBtn" className="btn link-primary" onClick={handleInsertNewRecord}><FontAwesomeIcon icon={faPlus} /></button>
                            </div>

                    }
                    <div id="recordsDiv">
                        {
                            formFields.recordList.map(record =>
                                <RecordForm data={record}
                                    key={record.id}
                                    update={updateRecord}
                                    remove={removeRecord}
                                    mode={mode} />

                            )
                        }
                    </div>
                </div>


                <div className="mb-3 row">
                    {
                        (mode === "NEW") ?
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
                        (mode === "EDIT") ?
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

export default JournalBodyItemForm;