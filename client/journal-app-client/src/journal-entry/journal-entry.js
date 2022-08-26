import React, { useEffect, useState, useRef } from 'react';
import {Link, useParams, useNavigate} from 'react-router-dom';
import JournalBodyItem from './journal-body-item';
import SimpleInput from '../util/components/simple-input';
import useSimpleState from '../util/hooks/useSimpleState';
import listUtil from '../util/functions/list-util';
import {useData} from '../contexts/dataContext';
import {useDashboard} from '../contexts/dashboardContext';

import { v4 as uuidv4 } from 'uuid';

import {DefaultJournalEntry} from './journal-entry-dto';

function JournalEntryPage(props) {
  const {createJournalEntry,updateJournalEntry, updateJournal, currentJournal} = useData();
  const {getJournalEntry} = useDashboard();

  const navigate = useNavigate();
  const {journalId, entryId} = useParams();

  //Refs
  const initData = useRef(DefaultJournalEntry);
  const initUsedTopics = useRef([]);
  const initTopicList = useRef([]);
  const usedTopics = useRef([]);

  // Simple States
  const [summary, setSummary, handleChangeSummary] = useSimpleState(initData.current.summary);
  const [dateOfEntry, setDateOfEntry, handleChangeDateOfEntry] = useSimpleState(initData.current.dateOfEntry);
  const [endTime, setEndTime, handleChangeEndTime] = useSimpleState(initData.current.endTime);
  const [overview, setOverview, handleChangeOverview] = useSimpleState(initData.current.overview);
  const [topic, setTopic, handleChangeTopic] = useSimpleState("");
  const [newTopic, setNewTopic, handleChangeNewTopic] = useSimpleState("");
  const [description, setDescription, handleChangeDescription] = useSimpleState("");

  // List States
  const [topicList, setTopicList] = useState([]);
  const [journalBodyItems, setJournalBodyItems] = useState(initData.current.journalBodyItems);
  const [recordList, setRecordList] = useState([]);

  useEffect(()=>{
    async function loadData(){
      const currentEntry = await getJournalEntry(entryId);
      if (currentEntry){
        initData.current=currentEntry
        initUsedTopics.current=initData.current.journalBodyItems.map(journalBodyItem => journalBodyItem.topic);
        resetAll();
      }
    }
    loadData();
  },[]);

  useEffect(()=>{
    async function loadInitTopics(){
      if (currentJournal){
        setTopicList(currentJournal.schemas.map(schema=>schema.topic));
        initTopicList.current=currentJournal.schemas.map(schema=> schema.topic);
      }
    }
    loadInitTopics();
  },[currentJournal])

  function resetAll(){
    setSummary(initData.current.summary);
    setDateOfEntry(initData.current.dateOfEntry);
    setEndTime(initData.current.endTime);
    setOverview(initData.current.overview);
    setJournalBodyItems([...initData.current.journalBodyItems]);
    usedTopics.current=[...initUsedTopics.current];
    setTopicList([...initTopicList.current]);

    clearJournalBodyForm();
  }

  const handleReset = e => {
    e.preventDefault();
    resetAll();
  }

  const clearJournalBodyForm = () => {
    setTopic("");
    setDescription("");
    setNewTopic("");
    listUtil(recordList, setRecordList, { type: "TRUNCATE" });
  }

  const saveJournalBodyItem = () => {
    let topicToSubmit = "";

    if (topic === "" && newTopic === "") {
      console.log("Cannot submit blank Topic...")
      return;
    }
    else if (topic === "" && newTopic !== "") {
      const newTopicToSave = newTopic.trim();
      topicToSubmit = newTopicToSave;

      if (!listUtil(topicList, setTopicList, { type: "CONTAINS", payload: newTopicToSave })){
        listUtil(topicList, setTopicList, { type: "INSERT", payload: newTopicToSave });
      }

    }
    else
      topicToSubmit = topic.trim();


    if (usedTopics.current.includes(topicToSubmit)) {
      console.log("Topic is already used...");
      return;
    }


    listUtil(journalBodyItems, setJournalBodyItems, {
      type: "INSERT",
      payload: {
        key: uuidv4(),
        topic: topicToSubmit,
        description: description.trim(),
        recordList: recordList
      }
    });
    usedTopics.current=[...usedTopics.current,topicToSubmit];
    clearJournalBodyForm();
  }


  const removeFromBody = key =>{ 
    const deleteUsedTopic = journalBodyItems.find(journalBodyItem=> journalBodyItem.key===key).topic;
    listUtil(journalBodyItems, setJournalBodyItems, { type: "DELETE", key: key });
    usedTopics.current= usedTopics.current.filter(usedTopic=> usedTopic !== deleteUsedTopic);
  }

  function getSchemaList(){
    let schemas=[];

    journalBodyItems.forEach(journalBodyItem=>{
      let schema = {
        topic: journalBodyItem.topic,
        records: []
      };
      journalBodyItem.recordList.forEach(record=>
        schema.records.push(record.key)
      )
      schemas.push(schema);
    });

    return schemas;
  }

  function updateJournalHistory(){
    let schemas=getSchemaList();

    console.log("Delta Schema : " + JSON.stringify(schemas)); 

    updateJournal(journalId, {...currentJournal,
      last_updated: new Date().toISOString(),
      schemas: schemas
    });
  }

  function getConsolidatedOutput(){
    let output = {
      journal: journalId,
      summary: summary.trim(),
      dateOfEntry: dateOfEntry,
      endTime: endTime,
      overview: overview.trim(),
      journalBodyItems: journalBodyItems
    }

    output.journalBodyItems.map(journalBodyItem=>{
      journalBodyItem.recordList.map(record=>{
        record.key= record.key.trim();
        record.value = record.value.trim();
      })
    });

    return output;
  }

  const handlePublish = e => {
    const output = getConsolidatedOutput();

    createJournalEntry(output).then((returnJournalEntry)=>{
      console.log("Published " + JSON.stringify(returnJournalEntry) + " to " + journalId);
      updateJournalHistory();
      navigate('/'+journalId);
    });
    
  }


  async function submitUpdate(){
    const output = getConsolidatedOutput();

    console.log("Update" + JSON.stringify(output));

    await updateJournalEntry(entryId,output);
    
    updateJournalHistory();

    navigate('/'+journalId);
  }

  const handleUpdate = e => {
    submitUpdate();
  }



  return (
    <div id="newEntryFormDiv" className="container page-div">
      <div className="col">
        <Link id="toDashboardButton" className="btn btn-outline-primary" to={"/"+journalId}>Go to Dashboard</Link>
      </div>
      <br/><br/>
      <form>
        <SimpleInput
          id="summaryField"
          value={summary}
          fieldName="summary"
          displayName="Summary"
          type="text"
          handleUpdate={handleChangeSummary}></SimpleInput>
        <div className="row">
          <div className="col">
            <SimpleInput
              id="dateOfEntryField"
              value={dateOfEntry}
              fieldName="dateOfEntry"
              displayName="Date (Default in UTC Time)"
              type="datetime-local"
              handleUpdate={handleChangeDateOfEntry}></SimpleInput>
          </div>
          <div className="col">
            <SimpleInput
              id="endTimeField"
              value={endTime}
              fieldName="endTime"
              displayName="End Time (optional)"
              type="time"
              handleUpdate={handleChangeEndTime}></SimpleInput>     
          </div>
        </div>  



        <SimpleInput
          id="overviewField"
          value={overview}
          fieldName="overview"
          displayName="Overview"
          type="textarea"
          handleUpdate={handleChangeOverview}></SimpleInput>

        <label htmlFor="journalBodyDiv" className="form-label">Body</label>
        <div id="journalBodyDiv">
          <JournalBodyItem
            topicList={topicList}
            newTopic={newTopic}
            data={{ topic: topic, description: description, recordList: recordList }}
            saveJournalBodyItem={saveJournalBodyItem}
            clearJournalBodyForm={clearJournalBodyForm}
            handleChangeTopic={handleChangeTopic}
            handleChangeNewTopic={handleChangeNewTopic}
            handleChangeDescription={handleChangeDescription}
            setRecordList={setRecordList}
            mode={"NEW"}></JournalBodyItem>
          <br />
          {
            journalBodyItems.map(journalBodyItem => (
              <div>
                <JournalBodyItem
                  mode={"VIEW"}
                  data={journalBodyItem}
                  removeFromBody={removeFromBody}
                  removeFromUsedTopics={() => usedTopics.current=usedTopics.current.filter(usedTopic=>usedTopic.id===journalBodyItem.topic.id)}
                  updateJournalBodyItem={newJournalBodyItem => listUtil(journalBodyItems, setJournalBodyItems, { type: "UPDATE", key: journalBodyItem.key, payload: newJournalBodyItem })}></JournalBodyItem>
                <br />
              </div>
            ))
          }
        </div>
      </form>
      <br/><br/><br/>
      <div id="formControlButtonsDiv" className="container-fluid fixed-bottom bg-dark">
        <br/>
        <div className="row">
          <div className="col">
            <button id="resetEntryFormButton" className="btn btn-danger" onClick={handleReset}>Reset</button>
          </div>
            <div className="col">
            {
              (props.mode==="NEW")?
              <button id="postEntryBtn" className="btn btn-primary float-end" onClick={handlePublish}>Post</button>
              :
                (props.mode==="EDIT")?
                  <button id="updateEntryBtn" className="btn btn-primary float-end" onClick={handleUpdate}>Save Changes</button>
                  :
                  <></>
            }
            </div>       
        </div>
      </div>

    </div>
  );

}

export default JournalEntryPage;