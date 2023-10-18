'use client'


import { JournalBodyItem, JournalEntryForm, defaultJournalRecord } from "../../../models/journalEntry";
import { Control, FieldError, FieldErrors, Merge, useFieldArray, UseFieldArrayRemove, UseFormRegister } from 'react-hook-form';
import JournalRecordForm from "./journalRecordForm";
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faPlus, faX } from '@fortawesome/free-solid-svg-icons'
import FormError from "../../formError";


interface JournalBodyItemFormProps {
    key: string,
    index: number,
    control: Control<JournalEntryForm, any>,
    register: UseFormRegister<JournalEntryForm>,
    disabled: boolean,
    errors: Merge<FieldError, FieldErrors<JournalBodyItem>> | undefined,
    remove: UseFieldArrayRemove
}

export default function JournalBodyItemForm(props: JournalBodyItemFormProps) {
    const { fields, append, remove } = useFieldArray({
        control: props.control,
        name: `journalBodyItems.${props.index}.recordList`
    });

    const handleAddRecord = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        append(defaultJournalRecord);
    }

    const handleRemove = (e: React.MouseEvent<HTMLButtonElement>) =>{
        e.preventDefault();
        props.remove(props.index);
    }

    return (
        <div className="form-child--bordered animated-entry-left">
            <div className="form-child__controls">
                <button className="form-child__delete-btn btn-link remove-btn" onClick={handleRemove}><FontAwesomeIcon icon={faX} /></button>
            </div>

            <div className="form__input">
                <label className="form__label">Topic</label>
                <input
                    id="topicInput"
                    className="form__input--text"
                    type="text"
                    {...props.register(`journalBodyItems.${props.index}.topic`)} 
                    disabled={props.disabled} />
            </div>
            <FormError field={props.errors?.topic} message={props.errors?.topic?.message} />

            <div className="form__input">
                <label className="form__label">Description</label>
                <textarea
                    id="descriptionInput"
                    className="form__input--textarea"
                    {...props.register(`journalBodyItems.${props.index}.description`)} 
                    disabled={props.disabled} />
            </div>
            <FormError field={props.errors?.description} message={props.errors?.description?.message} />

            <div className="form-child-list">
                <div className="form-child-list__header">
                    <label className="form__label"><h3>Records</h3></label>
                    {
                        (props.disabled)?
                            <></>
                        :
                            <button className="form-child-list__add-btn btn-link add-btn" onClick={handleAddRecord}><FontAwesomeIcon icon={faPlus} /></button>
                    }
                </div>

                {
                    fields.map((field, index) => (
                        <JournalRecordForm key={field.id}
                            index={index}
                            parentIndex={props.index}
                            register={props.register} 
                            disabled={props.disabled} 
                            errors={props.errors?.recordList? props.errors.recordList[index]: undefined}
                            remove={remove} />
                    ))
                }
            </div>
        </div>
    )
}