'use client'

import Link from "next/link"
import { JournalEntry } from "../../models/journalEntry"
import JournalEntrySummaryCard from "./journalEntries/journalEntrySummaryCard"
import { useState } from "react"

interface JournalEntriesScreenProps{
    journalId: string,
    journalEntries : JournalEntry[]
}


export default function JournalEntriesScreen(props:JournalEntriesScreenProps) { 
    const [journalEntries, setJournalEntries] = useState<JournalEntry[]>(props.journalEntries);
    const [searchInput, setSearchInput] = useState("");

    const handleChangeSearch = (e:React.ChangeEvent<HTMLInputElement>) => {
        const searchInput = e.target.value;
        setSearchInput(searchInput);

        const results = props.journalEntries.filter(
            journalEntry => journalEntry.summary.includes(searchInput) || journalEntry.overview.includes(searchInput) 
        );

        setJournalEntries(results);
    }



    return (
        <div className="journal-entries-list">
            <div className="journal-entries-list__controls">
                <input className="search-bar" onChange={handleChangeSearch} placeholder="search" />
                <Link className="journal-entries-list__new-btn btn-link" href={`/${props.journalId}/entries/new`}>+New Entry</Link>
            </div>
            
            {
                (journalEntries && journalEntries.length>0)?
                    journalEntries.map(journalEntry => 
                        <JournalEntrySummaryCard key={journalEntry.entryId} journalId={props.journalId} journalEntry={journalEntry} />
                    )
                :
                    (searchInput != "")?
                        <p> This journal is empty.</p>
                    :
                        <p> No entries were found... </p>
            }

        </div>
    )

}