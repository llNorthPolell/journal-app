'use client'


import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { JournalEntryForm, JournalRecord } from "../../../models/journalEntry";
import { FieldError, FieldErrors, Merge, UseFieldArrayRemove, UseFormRegister } from 'react-hook-form';
import { faX } from '@fortawesome/free-solid-svg-icons';
import FormError from "../../formError";

interface JournalRecordProps {
    key: string,
    index: number,
    parentIndex: number,
    register: UseFormRegister<JournalEntryForm>,
    disabled: boolean,
    errors: Merge<FieldError, FieldErrors<JournalRecord>> | undefined,
    remove: UseFieldArrayRemove
}

export default function JournalRecordForm(props: JournalRecordProps) {

    const handleRemove = (e: React.MouseEvent<HTMLButtonElement>) => {
        e.preventDefault();
        props.remove(props.index);
    }

    return (
        <div className="form-child--line animated-entry-left">
            <label className="form__label">Key</label>
            <div>
                <input
                    id="keyInput"
                    className="form__input--text"
                    type="text"
                    {...props.register(`journalBodyItems.${props.parentIndex}.recordList.${props.index}.recKey`)}
                    disabled={props.disabled} />
                <FormError field={props.errors?.recKey} message={props.errors?.recKey?.message} />
            </div>

            <label className="form__label">Value</label>
            <div>
                <input
                    id="valueInput"
                    className="form__input--text"
                    type="text"
                    {...props.register(`journalBodyItems.${props.parentIndex}.recordList.${props.index}.recValue`)}
                    disabled={props.disabled} />
                <FormError field={props.errors?.recValue} message={props.errors?.recValue?.message} />
            </div>

            <button className="form-child__delete-btn btn-link remove-btn" onClick={handleRemove}><FontAwesomeIcon icon={faX} /></button>
        </div>
    )
}