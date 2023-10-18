'use client'
import Link from "next/link"
import { JournalEntry } from "../../../models/journalEntry"

interface JournalEntryFormProps{
    journalId : string,
    journalEntry : JournalEntry
}

export default function JournalEntryView (props: JournalEntryFormProps){
    return (
        <div className="journal-entry-view">
            <div className="journal-entry-view__controls">
                <Link className="back-btn btn-link-outline" href={`/${props.journalId}/entries`}>&#8592; Back</Link>
            </div>

            <div className="journal-entry-view__contents animated-entry-top">
                <div className="journal-entry-view__header">   
                    <h1 className="journal-entry-view__summary">{props.journalEntry.summary}</h1>
                    <p className="journal-entry-view__date paragraph">{props.journalEntry.dateOfEntry}</p>
                    
                </div>
                <div className="journal-entry-view__body">
                    <p className="journal-entry-view__overview paragraph"> {props.journalEntry.overview}</p>
                    {
                        (props.journalEntry.journalBodyItems.map(journalBodyItem=>
                            <div className="journal-entry-view__journal-body-item">
                                <h3 className="journal-entry-view__topic heading-tertiary">{journalBodyItem.topic}</h3>
                                <p className="journal-entry-view__description paragraph">{journalBodyItem.description}</p>

                                <table className="journal-entry-view__record-table">
                                    <thead>
                                        <tr>
                                            <th><p className="paragraph">Key</p></th>
                                            <th><p className="paragraph">Value</p></th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                    {
                                        (journalBodyItem.recordList.map(record=>
                                            <tr>
                                                <td>
                                                    <p className="journal-entry-view__record-key paragraph">{record.recKey}</p>
                                                </td>
                                                <td>
                                                    <p className="journal-entry-view__record-value paragraph">{record.recValue}</p>
                                                </td>
                                            </tr>
                                        ))

                                    }
                                    </tbody>
                                </table>
                            </div>
                        ))
                    }
                
                </div>
            </div>        
        </div>


    )
}