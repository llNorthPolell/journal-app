import React, {useEffect, useState} from 'react';
import {Link, useParams} from "react-router-dom";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faGear } from '@fortawesome/free-solid-svg-icons'

import {useDashboard} from '../contexts/dashboardContext';

import PicCarouselWidget from './widgets/pic-carousel/pic-carousel';
import LastEntryWidget from './widgets/last-entry/last-entry';
import LineGraphWidget from './widgets/chart/line-graph';

import SearchResults from './search/search-results';
import WidgetMenuModal from './widgetMenu/widget-menu';

function DashboardPage(props){
    const {journalId} = useParams();

    const { filterJournalEntries, dashboardWidgetContents, currentJournal, saveDashboard,discardChangeDashboard} = useDashboard();
    const [contents,setContents] = useState([]);
    const [name, setName] = useState("");

    const [topics, setTopics] = useState([]);
    const [searchInput, setSearchInput] = useState("");
    const [searchResults,setSearchResults] = useState([]);

    const [mode, setMode] = useState("VIEW");

    useEffect(()=>{
        if (currentJournal){
            setTopics(currentJournal.schemas.map(schema=> schema.topic));
            setName(currentJournal.name);
        }
    },[currentJournal]);

    useEffect(()=>{
        setContents([...dashboardWidgetContents]);
    },[dashboardWidgetContents]);

    const handleChangeSearchInput = e => {
        e.preventDefault();
        setSearchInput(e.target.value);
    }

    useEffect(()=>{
        async function triggerSearch(){
            setSearchResults((searchInput==="")? [] : await filterJournalEntries(searchInput));
        }
        triggerSearch();
    }, [searchInput]);
    
    const handleToEditMode = e =>{
        e.preventDefault();
        setMode('EDIT');   
    }

    const handleSaveDashboard = e =>{
        e.preventDefault();
        saveDashboard();
        setMode("VIEW");
    }

    const handleCancelChangeDashboard = e => {
        e.preventDefault();
        discardChangeDashboard();
        setMode("VIEW");
    }

    return (
        <div id="dashboardDiv" className="container">
            <div className="row">
                {
                    (mode === "VIEW")?
                        <>
                            <div className="input-group col mb-3">
                                <input id="searchbar" className="form-control" type="text" list="searchOptions" placeholder="search" value={searchInput} onChange={handleChangeSearchInput}/>
                                <datalist id="searchOptions">
                                    {
                                        topics.map(topic=>
                                            <option value={topic}></option>
                                        )
                                    }
                                </datalist>
                            </div>

                    
                            <div className="col mb-3">
                                <Link id="newEntryBtn" className="btn btn-primary" to={"/"+journalId+"/new"}>+ New Entry </Link>
                            </div>
                        </>
                    : <></>
                }
                {
                    (mode === "VIEW")?
                        <div className="col">
                            <button id="editModeBtn" className="btn btn-secondary" onClick={handleToEditMode}><FontAwesomeIcon icon={faGear} /></button>
                        </div>
                    :
                        <>
                            <div className="col">
                                <WidgetMenuModal></WidgetMenuModal>
                            </div>
                            <div className="col">
                                    <button id="cancelDashboardChangeBtn" className="btn btn-outline-secondary" onClick={handleCancelChangeDashboard}>Cancel</button>
                                    <button id="saveDashboardChangeBtn" className="btn btn-primary" onClick={handleSaveDashboard}>Save Dashboard</button>
                            </div>
                        </>


                }
                
            </div>

           
            <br/><br/>

            {
                (searchInput=="")?
                    <div >
                        <h2>{name}</h2>
                        <div id="dashboardContentsDiv" className="container-fluid"> 
                        {
                            contents.map(
                                widget=>(
                                    (widget.type=="last-entry")?
                                        <LastEntryWidget lastEntry={widget.payload}></LastEntryWidget>
                                    :
                                    (widget.type=="pic-carousel")?
                                        <PicCarouselWidget picList={widget.payload}></PicCarouselWidget>
                                    :
                                    (widget.type=="line-graph")?
                                        <LineGraphWidget title={widget.payload.title} labels={widget.payload.labels} data={widget.payload.data}></LineGraphWidget>
                                    :
                                    null
                                )
                            ) 
                        }
                        </div>
                    </div>
                :
                    <SearchResults searchResults={searchResults}></SearchResults>
            }
        </div>
    );

}
export default DashboardPage;