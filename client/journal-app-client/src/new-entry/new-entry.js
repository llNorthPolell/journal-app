import React, {useState} from 'react';
import JournalBodyItemForm from './journal-body-item-form';
import JournalBodyItem from './journal-body-item';
import SimpleInput from '../util/components/simple-input';
import useSimpleState from '../util/hooks/useSimpleState';
import useListState from '../util/hooks/useListState';
import listUtil from '../util/functions/list-util';

import {v4 as uuidv4} from 'uuid';


function NewEntryPage(){
  // for defaulting date field to today
  const today = new Date();
  const day = today.getDate();
  const month = ((today.getMonth()>9)?'':'0') + (today.getMonth() +1);
  const year = today.getFullYear();

  // Simple States
  const [journalCollection,setJournalCollection,handleChangeJournalCollection] = useSimpleState("");
  const [summary,setSummary,handleChangeSummary] = useSimpleState("");
  const [dateOfEntry,setDateOfEntry,handleChangeDateOfEntry] = useSimpleState(year+"-"+month+"-"+day);
  const [overview,setOverview,handleChangeOverview] = useSimpleState("");
  const [topic,setTopic,handleChangeTopic] = useSimpleState("");
  const [newTopic,setNewTopic,handleChangeNewTopic] = useSimpleState("");
  const [description,setDescription,handleChangeDescription] = useSimpleState("");

  // List States
  const [topicList,setTopicList]=useState([]);
  const [journalBodyItems,setJournalBodyItems]=useState([]);
  const [usedTopics,setUsedTopics]=useState([]);
  const [recordList,setRecordList]=useState([]);
  //const [recordList, setRecordList, insertNewRecord, updateRecord, removeRecord] = useListState([]);


  const resetAll = e => {
    e.preventDefault();
    setJournalCollection("");
    setSummary("");
    setDateOfEntry(year+"-"+month+"-"+day);
    setOverview("");
    listUtil(journalBodyItems,setJournalBodyItems,{type:"TRUNCATE"});
    listUtil(usedTopics,setUsedTopics,{type:"TRUNCATE"});
    listUtil(topicList,setTopicList,{type:"TRUNCATE"});

    resetJournalBodyForm();
  }


  const resetJournalBodyForm = () => {
    setTopic("");
    setDescription("");
    setNewTopic("");
    listUtil(recordList,setRecordList,{type:"TRUNCATE"});
  }

  const saveJournalBodyItem = () =>{
    let topicToSubmit = "";

    if (topic=="" && newTopic==""){
        console.log("Cannot submit blank Topic...")
        return;
    }
    else if (topic=="" && newTopic!="") {
        topicToSubmit = newTopic;

        if(!listUtil(topicList,setTopicList,{type:"CONTAINS",payload:newTopic}))
          listUtil(topicList,setTopicList,{type:"INSERT",payload:newTopic});
    }
    else 
        topicToSubmit = topic;
    
        
    if(listUtil(usedTopics,setUsedTopics,{type:"CONTAINS",payload:topicToSubmit})){
      console.log("Topic is already used...");
      return;
    }


    listUtil(journalBodyItems,setJournalBodyItems,{
      type:"INSERT", 
      payload:{
        id:uuidv4(),
        topic:topicToSubmit,
        description:description,
        records:recordList}
      });
    listUtil(usedTopics,setUsedTopics,{type:"INSERT",payload:topicToSubmit});
    resetJournalBodyForm();
  }



  return (
    <div id="newEntryFormDiv" className="container">
      <form>
        <SimpleInput 
          id="collectionDropdown" 
          value={journalCollection} 
          fieldName="collection" 
          type="select" 
          optionList={["Select Collection", "Test Collection"]} 
          handleUpdate={handleChangeJournalCollection}></SimpleInput>  
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
          <JournalBodyItemForm
            topicList={topicList} 
            newTopic={newTopic}
            data={{topic: topic, description: description, recordList: recordList}}
            saveJournalBodyItem={saveJournalBodyItem}
            resetJournalBodyForm={resetJournalBodyForm}
            handleChangeTopic={handleChangeTopic}
            handleChangeNewTopic={handleChangeNewTopic}
            handleChangeDescription={handleChangeDescription}
            setRecordList={setRecordList}
            >
          </JournalBodyItemForm>
          <br/>
            {
              journalBodyItems.map(journalBodyItem => (
                <JournalBodyItem 
                  data = {journalBodyItem}
                  key={journalBodyItem.id} 
                  removeFromBody={()=> listUtil(journalBodyItems,setJournalBodyItems,{type:"DELETE",id:journalBodyItem.id})}
                  removeFromUsedTopics={()=> listUtil(usedTopics,setUsedTopics,{type:"DELETE",payload:journalBodyItem.topic})}
                  >
                </JournalBodyItem>
              ))
            }
        </div>
      </form>
      <div id="formControlButtonsDiv" className="container fixed-bottom">
        <div className="mb-3 row">
            <div className="col">
                <button id="clearEntryFormButton" className="btn btn-danger" onClick={resetAll}>Clear</button> 
            </div>
            <div className="col">
                <button id="postEntry" className="btn btn-primary float-end" onClick={resetAll}>Post</button> 
            </div>                   
        </div>
      </div>

    </div>
  );

}

export default NewEntryPage;