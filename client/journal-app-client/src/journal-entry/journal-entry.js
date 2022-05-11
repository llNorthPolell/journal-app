import React, {useState} from 'react';
import JournalBodyItem from './journal-body-item';
import SimpleInput from '../util/components/simple-input';
import useSimpleState from '../util/hooks/useSimpleState';
import listUtil from '../util/functions/list-util';

import {v4 as uuidv4} from 'uuid';


function JournalEntryPage(props){
  const data = props.data;
  const initUsedTopics = data.journalBodyItems.map(journalBodyItem => journalBodyItem.topic);

  // Simple States
  const [journalCollection,setJournalCollection,handleChangeJournalCollection] = useSimpleState(data.journalCollection);
  const [summary,setSummary,handleChangeSummary] = useSimpleState(data.summary);
  const [dateOfEntry,setDateOfEntry,handleChangeDateOfEntry] = useSimpleState(data.dateOfEntry);
  const [overview,setOverview,handleChangeOverview] = useSimpleState(data.overview);
  const [topic,setTopic,handleChangeTopic] = useSimpleState("");
  const [newTopic,setNewTopic,handleChangeNewTopic] = useSimpleState("");
  const [description,setDescription,handleChangeDescription] = useSimpleState("");

  // List States
  const [topicList,setTopicList]=useState(props.topicList);
  const [journalBodyItems,setJournalBodyItems]=useState(data.journalBodyItems);
  const [usedTopics,setUsedTopics]=useState(initUsedTopics);
  const [recordList,setRecordList]=useState([]);

  // Other
  const [editId, setEditId] = useState("");

  const resetAll = e => {
    e.preventDefault();
    setJournalCollection(data.journalCollection);
    setSummary(data.summary);
    setDateOfEntry(data.dateOfEntry);
    setOverview(data.overview);
    setJournalBodyItems([...data.journalBodyItems]);
    setUsedTopics([...initUsedTopics]);
    setTopicList([...props.topicList]);

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
        recordList:recordList}
      });
    listUtil(usedTopics,setUsedTopics,{type:"INSERT",payload:topicToSubmit});
    resetJournalBodyForm();
  }


  const takeEdit = (id) => {
    if (editId==""){
      setEditId(id);
      return;
    }
 
    setEditId(id);
  }

  const releaseEdit = () => {
    setEditId("");
  }

  const publish = e => {
    let output = {
      journalCollection: journalCollection,
      summary: summary,
      dateOfEntry: dateOfEntry,
      overview: overview,
      journalBodyItems:journalBodyItems
    }

    console.log("Published " + output + " to " + journalCollection);
  }


  return (
    <div id="newEntryFormDiv" className="container">
      <form>
        <SimpleInput 
          id="collectionDropdown" 
          value={journalCollection} 
          fieldName="collection" 
          type="select" 
          optionList={props.journalCollections} 
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
          <JournalBodyItem 
            topicList={topicList} 
            newTopic={newTopic}
            data={{topic: topic, description: description, recordList: recordList}}
            saveJournalBodyItem={saveJournalBodyItem}
            resetJournalBodyForm={resetJournalBodyForm}
            handleChangeTopic={handleChangeTopic}
            handleChangeNewTopic={handleChangeNewTopic}
            handleChangeDescription={handleChangeDescription}
            setRecordList={setRecordList}
            mode={"NEW"}></JournalBodyItem>
          <br/>
            {
              journalBodyItems.map(journalBodyItem => (
                <div> 
                  <JournalBodyItem 
                    mode={(editId==journalBodyItem.id)? "EDIT":"VIEW"}
                    data = {journalBodyItem}
                    key={journalBodyItem.id} 
                    removeFromBody={()=> listUtil(journalBodyItems,setJournalBodyItems,{type:"DELETE",id:journalBodyItem.id})}
                    removeFromUsedTopics={()=> listUtil(usedTopics,setUsedTopics,{type:"DELETE",payload:journalBodyItem.topic})}
                    updateJournalBodyItem={newJournalBodyItem=> listUtil(journalBodyItems,setJournalBodyItems,{type:"UPDATE",id:journalBodyItem.id,payload:newJournalBodyItem})}
                    takeEdit={takeEdit}
                    releaseEdit={releaseEdit}></JournalBodyItem>
                    <br/>
                </div>
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
                <button id="postEntry" className="btn btn-primary float-end" onClick={publish}>Post</button> 
            </div>                   
        </div>
      </div>

    </div>
  );

}

export default JournalEntryPage;