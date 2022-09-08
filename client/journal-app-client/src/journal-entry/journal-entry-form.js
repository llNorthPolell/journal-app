import React, {useState,useEffect,useRef,useMemo} from 'react';
import { v4 as uuidv4 } from 'uuid';

import SimpleInput from '../util/components/simple-input';
import useSimpleForm from '../util/hooks/useSimpleForm';
import useError from '../util/hooks/useError';

import JournalBodyItemForm from './journal-body-item/journal-body-item';
import JournalEntryToolbar from './journal-entry-toolbar';

import {DefaultJournalBodyItem} from './journal-body-item/journal-body-item-dto';

function JournalEntryForm(props) {
    const [formFields,updateForm,submit,resetForm] = useSimpleForm(props.data,props.submit);
    
    const [errors, throwError,clearErrors] = useError();

    const initTopicList = useRef([]);
    const [topicList,setTopicList] = useState([])

    useEffect(()=>{
        resetForm();
    }, [props.data])

    useEffect(()=>{
        console.log("Current Journal in Journal Entry Form: "+JSON.stringify(props.journal));
        if (props.journal){
            initTopicList.current = (props.journal.schemas.map(schema=> schema.topic));
            setTopicList([...initTopicList.current]);
            console.log("Topic list: "+JSON.stringify(topicList));
        }
    },[props.journal]);

    const usedTopics = useMemo(()=>
        formFields.journalBodyItems.map(journalBodyItem=> journalBodyItem.topic)
    ,[formFields.journalBodyItems]);


    const handleChange = e=>{
        updateForm({[e.target.name]: e.target.value});
    }

    const handleReset = e => {
        e.preventDefault();
        resetForm();
        setTopicList([...initTopicList.current]);
    }

    const handleSubmit = e => {
        e.preventDefault();
        submit();
    }

    const saveJournalBodyItem = journalBodyItem => {
        let {newTopic,...payload} = journalBodyItem;

        if (newTopic)
            payload = {...journalBodyItem,topic:journalBodyItem.newTopic};

        if (usedTopics.includes(payload.topic))
            return throwError("Topic is already used...",["journalEntry","journalBodyItem"+((payload.id)?"["+payload.id+"]":""),"topic"]);
        

        payload.id = uuidv4();


        updateForm({journalBodyItems:[...formFields.journalBodyItems, payload]});
        clearErrors("journalBodyItem");

        
        if (!topicList.includes(payload.topic))
            setTopicList([...topicList,payload.topic]);
    }

    const updateJournalBodyItem = journalBodyItem => {
        let payload = { ...journalBodyItem };

        clearErrors("journalBodyItem[" + payload.id + "]");

        updateForm({
            journalBodyItems: formFields.journalBodyItems.map(journalBodyItem =>
                (journalBodyItem.id === payload.id) ?
                    payload : journalBodyItem
            )
        });
        clearErrors("journalBodyItem[" + payload.id + "]");
    }

    const removeJournalBodyItem = id => {
        updateForm({journalBodyItems:formFields.journalBodyItems.filter(journalBodyItem=> journalBodyItem.id!==id)});
    }

    return (
        <div className="container">
            {
                (errors.length>0)?
                <div className="errorDiv bg-danger text-light">
                    {errors.map(error=> <p>{error.message}</p>)}
                </div>
                :
                <></>
            }
            <form>
                <SimpleInput
                    id="summaryField"
                    value={formFields.summary}
                    fieldName="summary"
                    displayName="Summary"
                    type="text"
                    handleUpdate={handleChange}/>
                <div className="row">
                    <div className="col">
                        <SimpleInput
                            id="dateOfEntryField"
                            value={formFields.dateOfEntry}
                            fieldName="dateOfEntry"
                            displayName="Date (Default in UTC Time)"
                            type="datetime-local"
                            handleUpdate={handleChange}/>
                    </div>
                    <div className="col">
                        <SimpleInput
                            id="endTimeField"
                            value={formFields.endTime}
                            fieldName="endTime"
                            displayName="End Time (optional)"
                            type="time"
                            handleUpdate={handleChange}/>
                    </div>
                </div>

                <SimpleInput
                    id="overviewField"
                    value={formFields.overview}
                    fieldName="overview"
                    displayName="Overview"
                    type="textarea"
                    handleUpdate={handleChange}/>

                <label htmlFor="journalBodyDiv" className="form-label">Body</label>
                <div id="journalBodyDiv">
                    <JournalBodyItemForm 
                        mode="NEW"
                        data={DefaultJournalBodyItem}
                        save={saveJournalBodyItem}
                        topicList={topicList}/>
                    {
                        formFields.journalBodyItems.map(journalBodyItem=>
                            <JournalBodyItemForm 
                                key={journalBodyItem.id}
                                topicList={topicList}
                                data={journalBodyItem}
                                mode="VIEW"
                                save={updateJournalBodyItem}
                                remove={removeJournalBodyItem}/>
                        )
                    }
                </div>
            </form>
            <br/><br/><br/>
            <JournalEntryToolbar handleSubmit={handleSubmit} handleReset={handleReset} mode={props.mode}/>
        </div>
    )
}
export default JournalEntryForm