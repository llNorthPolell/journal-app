import React, {useEffect, useState} from 'react';
import {Link, useParams} from "react-router-dom";

import {useDashboard} from '../contexts/dashboardContext';

import PicCarouselWidget from './widgets/pic-carousel/pic-carousel';
import LastEntryWidget from './widgets/last-entry/last-entry';
import LineGraphWidget from './widgets/chart/line-graph';

import SearchResults from './search/search-results';

function DashboardPage(props){
    const {journalId} = useParams();

    const {loadDashboard, journalDoc, filterJournalEntries, dashboardWidgetContents} = useDashboard();
    const [contents,setContents] = useState([]);
    const [name, setName] = useState("");

    const [topics, setTopics] = useState([]);
    const [searchInput, setSearchInput] = useState("");
    const [searchResults,setSearchResults] = useState([]);

    

    useEffect(async ()=>{
        await loadDashboard(journalId);
    }, []);

    useEffect(()=>{
        if (journalDoc!=null){
            setTopics([...journalDoc.topics]);
            setName(journalDoc.name);
        }
    },[journalDoc]);

    useEffect(()=>{
        setContents([...dashboardWidgetContents]);
    },[dashboardWidgetContents]);

    const handleChangeSearchInput = e => {
        e.preventDefault();
        setSearchInput(e.target.value);
    }

    useEffect(()=>{
        setSearchResults((searchInput==="")? [] : filterJournalEntries(searchInput));
    }, [searchInput]);
    

    return (
        <div id="dashboardDiv" className="container">
            <div className="row">
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
                    <Link id="newEntryBtn" className="btn btn-primary" to={"/journal-app/"+journalId+"/new"}>+ New Entry </Link>
                </div>
            </div>

           
            <br/><br/>

            {
                (searchInput=="")?
                    <div id="dashboardContentsDiv">
                        <h2>{name}</h2>
                        <div className="row row-cols-4"> 
                        {
                            contents.map(
                                widget=>(
                                    (widget.type=="last-entry")?
                                        <LastEntryWidget description={widget.payload}></LastEntryWidget>
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