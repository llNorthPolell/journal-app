import React, { useEffect, useState } from 'react';
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
  let data = DefaultJournalEntry;
  let initUsedTopics = data.journalBodyItems.map(journalBodyItem => journalBodyItem.topic);

  const {createJournalEntry, updateJournal, getJournalDoc, currentJournal} = useData();
  const {getJournalEntry} = useDashboard();

  const navigate = useNavigate();
  const {journalId, entryId} = useParams();
  
 
  // Simple States
  const [summary, setSummary, handleChangeSummary] = useSimpleState(data.summary);
  const [dateOfEntry, setDateOfEntry, handleChangeDateOfEntry] = useSimpleState(data.dateOfEntry);
  const [overview, setOverview, handleChangeOverview] = useSimpleState(data.overview);
  const [topic, setTopic, handleChangeTopic] = useSimpleState("");
  const [newTopic, setNewTopic, handleChangeNewTopic] = useSimpleState("");
  const [description, setDescription, handleChangeDescription] = useSimpleState("");

  // List States
  const [topicList, setTopicList] = useState([]);
  const [journalBodyItems, setJournalBodyItems] = useState(data.journalBodyItems);
  const [usedTopics, setUsedTopics] = useState(initUsedTopics);
  const [recordList, setRecordList] = useState([]);

  useEffect(()=>{
    async function loadInitTopics(){
      if (currentJournal){
        setTopicList(currentJournal.schemas.map(schema=>schema.topic));
      }
    }
    loadInitTopics();
  },[currentJournal])

  
  useEffect(()=>{
    async function loadData(){
      if (!entryId) return;
      data = await getJournalEntry(entryId);
      initUsedTopics = data.journalBodyItems.map(journalBodyItem => journalBodyItem.topic);
      resetAll();
    }
    loadData();
  },[]);

  function resetAll(){
    setSummary(data.summary);
    setDateOfEntry(data.dateOfEntry);
    setOverview(data.overview);
    setJournalBodyItems([...data.journalBodyItems]);
    setUsedTopics([...initUsedTopics]);
    setTopicList([getJournalDoc(journalId).topics]);

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
      topicToSubmit = newTopic;

      if (!listUtil(topicList, setTopicList, { type: "CONTAINS", payload: newTopic })){
        listUtil(topicList, setTopicList, { type: "INSERT", payload: newTopic });
      }

    }
    else
      topicToSubmit = topic;


    if (listUtil(usedTopics, setUsedTopics, { type: "CONTAINS", payload: topicToSubmit })) {
      console.log("Topic is already used...");
      return;
    }


    listUtil(journalBodyItems, setJournalBodyItems, {
      type: "INSERT",
      payload: {
        key: uuidv4(),
        topic: topicToSubmit,
        description: description,
        recordList: recordList
      }
    });
    listUtil(usedTopics, setUsedTopics, { type: "INSERT", payload: topicToSubmit });
    clearJournalBodyForm();
  }


  const removeFromBody = key =>{ 
    listUtil(journalBodyItems, setJournalBodyItems, { type: "DELETE", key: key });
  }

  const publish = e => {
    let output = {
      journal: journalId,
      summary: summary,
      dateOfEntry: dateOfEntry,
      overview: overview,
      journalBodyItems: journalBodyItems
    }

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


    console.log("Delta Schema : " + JSON.stringify(schemas)); 

    createJournalEntry(output).then((returnJournalEntry)=>{
      console.log("Published " + JSON.stringify(returnJournalEntry) + " to " + journalId);
      updateJournal(journalId, {
        last_updated: new Date().toISOString(),
        schemas: schemas
      });
      navigate('/'+journalId);
    });
    
  }


  return (
    <div id="newEntryFormDiv" className="container">
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
        <SimpleInput
          id="dateOfEntryField"
          value={dateOfEntry}
          fieldName="dateOfEntry"
          displayName="Date"
          type="datetime-local"
          handleUpdate={handleChangeDateOfEntry}></SimpleInput>
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
                  key={journalBodyItem.key}
                  removeFromBody={removeFromBody}
                  removeFromUsedTopics={() => listUtil(usedTopics, setUsedTopics, { type: "DELETE", payload: journalBodyItem.topic })}
                  updateJournalBodyItem={newJournalBodyItem => listUtil(journalBodyItems, setJournalBodyItems, { type: "UPDATE", id: journalBodyItem.id, payload: newJournalBodyItem })}></JournalBodyItem>
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
            <button id="clearEntryFormButton" className="btn btn-danger" onClick={handleReset}>Clear</button>
          </div>
          <div className="col">
            <button id="postEntry" className="btn btn-primary float-end" onClick={publish}>Post</button>
          </div>
        </div>
      </div>

    </div>
  );

}

export default JournalEntryPage;