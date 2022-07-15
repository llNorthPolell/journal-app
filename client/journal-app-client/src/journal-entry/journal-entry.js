import React, { useState } from 'react';
import {Link, useParams, useNavigate} from 'react-router-dom';
import JournalBodyItem from './journal-body-item';
import SimpleInput from '../util/components/simple-input';
import useSimpleState from '../util/hooks/useSimpleState';
import listUtil from '../util/functions/list-util';
import {useData} from '../contexts/dataContext';

import { v4 as uuidv4 } from 'uuid';


function JournalEntryPage(props) {
  const data = props.data;
  const initUsedTopics = data.journalBodyItems.map(journalBodyItem => journalBodyItem.topic);

  const {createJournalEntry} = useData();

  const navigate = useNavigate();
  const {journalId} = useParams();

  // Simple States
  const [summary, setSummary, handleChangeSummary] = useSimpleState(data.summary);
  const [dateOfEntry, setDateOfEntry, handleChangeDateOfEntry] = useSimpleState(data.dateOfEntry);
  const [overview, setOverview, handleChangeOverview] = useSimpleState(data.overview);
  const [topic, setTopic, handleChangeTopic] = useSimpleState("");
  const [newTopic, setNewTopic, handleChangeNewTopic] = useSimpleState("");
  const [description, setDescription, handleChangeDescription] = useSimpleState("");

  // List States
  const [topicList, setTopicList] = useState(props.topicList);
  const [journalBodyItems, setJournalBodyItems] = useState(data.journalBodyItems);
  const [usedTopics, setUsedTopics] = useState(initUsedTopics);
  const [recordList, setRecordList] = useState([]);


  const resetAll = e => {
    e.preventDefault();
    setSummary(data.summary);
    setDateOfEntry(data.dateOfEntry);
    setOverview(data.overview);
    setJournalBodyItems([...data.journalBodyItems]);
    setUsedTopics([...initUsedTopics]);
    setTopicList([...props.topicList]);

    clearJournalBodyForm();
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

      if (!listUtil(topicList, setTopicList, { type: "CONTAINS", payload: newTopic }))
        listUtil(topicList, setTopicList, { type: "INSERT", payload: newTopic });
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

  const publish = e => {
    let output = {
      journal: journalId,
      summary: summary,
      dateOfEntry: dateOfEntry,
      overview: overview,
      journalBodyItems: journalBodyItems
    }

    createJournalEntry(output).then((returnJournal)=>{
      console.log("Published " + JSON.stringify(returnJournal) + " to " + journalId);
      navigate('/journal-app/'+journalId);
    });
    
  }


  return (
    <div id="newEntryFormDiv" className="container">
      <div className="col">
        <Link id="toDashboardButton" className="btn btn-outline-primary" to={"/journal-app/"+journalId}>Go to Dashboard</Link>
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
          type="date"
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
                  key={journalBodyItem.id}
                  removeFromBody={() => listUtil(journalBodyItems, setJournalBodyItems, { type: "DELETE", id: journalBodyItem.id })}
                  removeFromUsedTopics={() => listUtil(usedTopics, setUsedTopics, { type: "DELETE", payload: journalBodyItem.topic })}
                  updateJournalBodyItem={newJournalBodyItem => listUtil(journalBodyItems, setJournalBodyItems, { type: "UPDATE", id: journalBodyItem.id, payload: newJournalBodyItem })}></JournalBodyItem>
                <br />
              </div>
            ))
          }
        </div>
      </form>
      <br/><br/><br/>
      <div id="formControlButtonsDiv" className="container fixed-bottom">
        <br/>
        <div className="mb-3 row">
          <div className="col">
            <button id="clearEntryFormButton" className="btn btn-danger" onClick={resetAll}>Clear</button>
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