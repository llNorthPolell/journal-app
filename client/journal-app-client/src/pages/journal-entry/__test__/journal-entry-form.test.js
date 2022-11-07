import React from "react";
import {render, fireEvent} from "@testing-library/react";
import "@testing-library/jest-dom/extend-expect";
import userEvent from '@testing-library/user-event';

import JournalEntryForm from "../journal-entry-form";
import {DefaultJournalEntry} from "../journal-entry-dto";


const TestEntryForm = ({data,mode})=>{
    return (
        <JournalEntryForm data={data} mode={mode}/>
    )
}

const TestNewEntryForm=()=>{
    return (
        <TestEntryForm data={DefaultJournalEntry}  mode="NEW"/>
    )
}

const mockData={
    summary:"My First Post",
    dateOfEntry: "2022-10-02T18:11:37",
    endTime:"18:30",
    overview: "My first post for unit testing.",
    journalBodyItems:[
        {
            key:"33a66b75-3dcd-4811-bbbf-1ad3e07db15f",
            topic: "My first topic",
            description: "A description for my first topic",
            recordList: [
                {
                    id:"d0a6ca4f-f1c7-4992-88fe-4b87654e2007",
                    key:"a",
                    value:"1"
                },
                {
                    id:"597bf330-bf5d-4613-b762-49f327420d40",
                    key:"targetA",
                    value:"2"
                }
            ]
        }
    ]
}

const mockJournal={
    author:"abc",
    creation_timestamp:"2022-08-19T22:31:01.279Z",
    img:"asdf.png",
    last_updated:"2022-08-29T02:28:07.742Z",
    name:"test",
    permissions:"private",
    schemas:[
        {
            records:[
                "a","target a"
            ],
            topic:"My first topic"
        }
    ]
}

const TestExistingEntryForm=()=>{
    return (
        <TestEntryForm data={mockData} journal={mockJournal}  mode="UPDATE"/>
    )
}

describe("New Journal Form DOM Element Checks", ()=>{
    it("should display Summary Text Field",()=>{
        const form = render(<TestNewEntryForm/>);
        const summaryField = form.getByLabelText('Summary');
        expect(summaryField).toBeInTheDocument();
    })
    
    it("should display Date of Entry Text Field",()=>{
        const form = render(<TestNewEntryForm/>);
        const dateOfEntryField = form.getByLabelText('Date (Default in UTC Time)');
        expect(dateOfEntryField).toBeInTheDocument();
    })

    it("should display End Time Text Field",()=>{
        const form = render(<TestNewEntryForm/>);
        const endTimeField = form.getByLabelText('End Time (optional)');
        expect(endTimeField).toBeInTheDocument();
    })

    it("should display Overview Text Area",()=>{
        const form = render(<TestNewEntryForm/>);
        const overviewField = form.getByLabelText('Overview');
        expect(overviewField).toBeInTheDocument();
    })

    it("should display Journal Body Div",()=>{
        const form = render(<TestNewEntryForm/>);
        const journalBodyDiv = form.container.querySelector('#journalBodyDiv');
        expect(journalBodyDiv).toBeInTheDocument();
    })

    describe("Journal Body Div", ()=>{
        it("should contain one child",()=>{
            const form = render(<TestNewEntryForm/>);
            const journalBodyDiv = form.container.querySelector('#journalBodyDiv');
            expect(journalBodyDiv.childElementCount).toBe(1);
        })

        it("should have one option in the Topic Dropdown",()=>{
            const form = render(<TestNewEntryForm/>);
            const journalBodyDiv = form.container.querySelector('#journalBodyDiv');
            const newJournalBodyForm = journalBodyDiv.firstChild;
            const topicSelect = newJournalBodyForm.querySelector('#topicDropdown');
            expect(topicSelect.childElementCount).toBe(1);
        })

        it("should show Topic Dropdown with \"New Topic\" selected and display New Topic Field",()=>{
            const form = render(<TestNewEntryForm/>);
            const journalBodyDiv = form.container.querySelector('#journalBodyDiv');
            const newJournalBodyForm = journalBodyDiv.firstChild;
            const topicSelect = newJournalBodyForm.querySelector('#topicDropdown');
            const newTopicField = newJournalBodyForm.querySelector('#newTopicField');
            expect(topicSelect.value).toBe("");
            expect(newTopicField).toBeInTheDocument();
        })

    })
})


describe("Journal Entry Form with Existing Data DOM Element Checks",()=>{
    it("should have Summary Field populated with Mock Summary Value",()=>{
        const form = render(<TestExistingEntryForm/>);
        const summaryField = form.getByLabelText('Summary');
        expect(summaryField.value).toBe(mockData.summary);
    })
    
    it("should have Date of Entry Field populated with Mock Date of Entry Value",()=>{
        const form = render(<TestExistingEntryForm/>);
        const dateOfEntryField = form.getByLabelText('Date (Default in UTC Time)');
        expect(dateOfEntryField.value.split('.')[0]).toBe(mockData.dateOfEntry);
    })

    it("should display End Time Text Field",()=>{
        const form = render(<TestExistingEntryForm/>);
        const endTimeField = form.getByLabelText('End Time (optional)');
        expect(endTimeField.value).toBe(mockData.endTime);
    })

    it("should display Overview Text Area",()=>{
        const form = render(<TestExistingEntryForm/>);
        const overviewField = form.getByLabelText('Overview');
        expect(overviewField.value).toBe(mockData.overview);
    })

    describe("Journal Body Div", ()=>{
        it("should contain two children",()=>{
            const form = render(<TestExistingEntryForm/>);
            const journalBodyDiv = form.container.querySelector('#journalBodyDiv');
            expect(journalBodyDiv.childElementCount).toBe(2);
        })
        
        it("should have a used topic in Topic Dropdown",()=>{
            const form = render(<TestExistingEntryForm/>);
            const topicSelect = form.container.querySelector('#topicDropdown');
            expect(topicSelect.childElementCount).toBe(2);
        })
    })

})



describe("Functional Checks",()=>{
    it("should be able to type a Summary",()=>{
        const form = render(<TestNewEntryForm/>);
        const summaryField = form.getByLabelText('Summary');
        const input = "Unit Test";
        fireEvent.change(summaryField,{ target: {value: input}})
        expect(summaryField.value).toBe(input);
    })

    it("should be able to change the Date of Entry",()=>{
        const form = render(<TestNewEntryForm/>);
        const dateOfEntryField = form.getByLabelText('Date (Default in UTC Time)');
        const input = "2022-10-02T18:11:37";
        fireEvent.change(dateOfEntryField,{ target: {value: input}})
        expect(dateOfEntryField.value.split('.')[0]).toBe(input);
    })

    it("should be able to change the End Time",()=>{
        const form = render(<TestNewEntryForm/>);
        const endTimeField = form.getByLabelText('End Time (optional)');
        const input = "18:30";
        fireEvent.change(endTimeField,{ target: {value: input}})
        expect(endTimeField.value).toBe(input);
    })

    it("should display Overview Text Area",()=>{
        const form = render(<TestNewEntryForm/>);
        const overviewField = form.getByLabelText('Overview');
        const input = "Testing Testing 123!!!!";
        fireEvent.change(overviewField,{ target: {value: input}})
        expect(overviewField.value).toBe(input);
    })

    describe("Journal Body Item Form ",()=>{
        const addJournalBodyItem = (form,data)=>{
            const journalBodyDiv = form.container.querySelector('#journalBodyDiv');
            const newJournalBodyForm = journalBodyDiv.firstChild;
            const newRecordBtn = newJournalBodyForm.querySelector('#newRecordBtn');
            const recordsDiv = newJournalBodyForm.querySelector('#recordsDiv');

            const topicSelect = newJournalBodyForm.querySelector('#topicDropdown');
            const newTopicField = newJournalBodyForm.querySelector('#newTopicField');
            const descriptionField = newJournalBodyForm.getByLabelText("Description");
            const saveBtn = newJournalBodyForm.getByText('Add to Body', { selector: 'button' })

            if(data.topic)
                userEvent.selectOptions(topicSelect,data.topic);
            else 
                if(data.newTopic)
                    fireEvent.change(newTopicField,{target: {value: data.newTopicField}});

            if(data.description)    
                fireEvent.change(descriptionField,{target: {value: data.description}});

            if(data.recordList)
                data.recordList.forEach(record=>{
                    fireEvent.click(newRecordBtn);
                    const recordFieldsDiv = recordsDiv.querySelectorAll('.row')[recordsDiv.childElementCount -1];
                    const recordKeyField = recordFieldsDiv.querySelector('#recordKeyField');
                    const recordValueField = recordFieldsDiv.querySelector('#recordValueField');
                    fireEvent.change(recordKeyField,{target: {value: record.key}});
                    fireEvent.change(recordValueField,{target: {value: record.value}});
                })

            fireEvent.click(saveBtn);
        }

        it("should not show New Topic Field if topic is not set to \"New Topic\"",()=>{
            const form = render(<TestExistingEntryForm/>);
            const journalBodyDiv = form.container.querySelector('#journalBodyDiv');
            const newJournalBodyForm = journalBodyDiv.firstChild;
            const topicSelect = newJournalBodyForm.querySelector('#topicDropdown');

            const newTopicField = newJournalBodyForm.querySelector('#newTopicField');
            userEvent.selectOptions(topicSelect,"My first topic");

            expect(topicSelect.value).not.toBe("");
            expect(newTopicField).not.toBeInTheDocument();
        })

        it("should add a new set of Record Fields on clicking + sign",()=>{
            const form = render(<TestNewEntryForm/>);
            const journalBodyDiv = form.container.querySelector('#journalBodyDiv');
            const newJournalBodyForm = journalBodyDiv.firstChild;
            const newRecordBtn = newJournalBodyForm.querySelector('#newRecordBtn');
            const recordsDiv = newJournalBodyForm.querySelector('#recordsDiv');
            fireEvent.click(newRecordBtn);
            expect(recordsDiv.childElementCount).toBe(1);
        })

        it("should delete the correct set of Record Fields",()=>{
            const form = render(<TestNewEntryForm/>);
            const journalBodyDiv = form.container.querySelector('#journalBodyDiv');
            const newJournalBodyForm = journalBodyDiv.firstChild;
            const newRecordBtn = newJournalBodyForm.querySelector('#newRecordBtn');
            const recordsDiv = newJournalBodyForm.querySelector('#recordsDiv');

            fireEvent.click(newRecordBtn);
            const firstRecordFieldsDiv = recordsDiv.querySelectorAll('.row')[0];
            const firstRecordKeyField = firstRecordFieldsDiv.querySelector('#recordKeyField');
            const firstRecordValueField = firstRecordFieldsDiv.querySelector('#recordValueField');

            fireEvent.click(newRecordBtn);
            const secondRecordFieldsDiv = recordsDiv.querySelectorAll('.row')[1];
            const secondRecordKeyField = secondRecordFieldsDiv.querySelector('#recordKeyField');
            const secondRecordValueField = secondRecordFieldsDiv.querySelector('#recordValueField');
            const secondDeleteBtn = secondRecordFieldsDiv.querySelector('button');

            fireEvent.click(secondDeleteBtn);

            expect(recordsDiv.childElementCount).toBe(1);
            expect(firstRecordKeyField).toBeInTheDocument();
            expect(firstRecordValueField).toBeInTheDocument();
            expect(secondRecordKeyField).not.toBeInTheDocument();
            expect(secondRecordValueField).not.toBeInTheDocument();
        })


        it("should add new used topic to topic list after adding journal body item",()=>{
            const form = render(<TestNewEntryForm/>);
            const journalBodyDiv = form.container.querySelector('#journalBodyDiv');
            const newJournalBodyForm = journalBodyDiv.firstChild;
            addJournalBodyItem(form,{
                newTopic:"My Test Topic",
                description:"lalalala",
                recordList:[
                    {
                        key:"a",
                        value:"1"
                    }
                ]
            });

        })
    })

    
})