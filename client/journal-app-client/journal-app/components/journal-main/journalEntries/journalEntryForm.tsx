'use client'


import { JournalEntry, JournalEntrySchema, JournalEntryForm, defaultJournalEntryForm, defaultJournalBodyItem } from "../../../models/journalEntry";
import { SubmitHandler, useForm, useFieldArray } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import JournalBodyItemForm from "./journalBodyItemForm";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faPlus, faSave } from '@fortawesome/free-solid-svg-icons';
import Link from "next/link";
import FormError from "../../formError";
import { useRouter } from "next/navigation";
import {useEffect} from 'react';

interface JournalEntryFormProps{
    journalId: string,
    journalEntry? : JournalEntry,
    edit: boolean
}

const submitToApi = async (journalId: string, journalEntryForm : JournalEntryForm) =>{
    const url = '/api/entries';

    const body = {journalEntryForm: {...journalEntryForm}, journalId: journalId}

    const res = await fetch (
        url,
        {
            method: "POST",
            headers: {"Content-Type": "application/json"},
            body: JSON.stringify(body)
        }
    )
    const resBody = await res.json();
}


export default function JournalEntryForm (props : JournalEntryFormProps){
    const router = useRouter();
    const disabled = !props.edit;

    const {
        register,
        control,
        handleSubmit,
        formState: { errors, isValid, isSubmitSuccessful },
        trigger,
        reset,
        getValues
    } = useForm<JournalEntryForm>({
        defaultValues: props.journalEntry? props.journalEntry : defaultJournalEntryForm,
        resolver: zodResolver(JournalEntrySchema)
    });


    useEffect(()=>{
        if (isSubmitSuccessful)
            router.push(`/${props.journalId}/entries`)
    },[isSubmitSuccessful])


    const { fields, prepend, remove } = useFieldArray({
        control: control,
        name:'journalBodyItems'
    });


    const handleAddBody = (e : React.MouseEvent<HTMLButtonElement>)=>{
        e.preventDefault();
        prepend(defaultJournalBodyItem);
    }

    const onSubmit: SubmitHandler<JournalEntryForm> = async (entryToSave) => {
        console.log("Submit: " + JSON.stringify(entryToSave));
        console.log("Errors:" + JSON.stringify(errors));
        console.log("Is Valid: " + isValid);

        await submitToApi(props.journalId, entryToSave)
    }


    return (
        <div className="journal-entry-form">
            <form className="form" onSubmit={handleSubmit(onSubmit)}>
                <div className="form__controls">
                    <Link className="back-btn btn-link-outline" href={`/${props.journalId}/entries`}>&#8592; Back</Link>
                    <button className="form__submit-btn btn-link" type="submit"><FontAwesomeIcon icon={faSave} /></button>
                </div>

                <div className="form__input">
                    <label className="form__label">Summary</label>
                    <input
                        id="summaryInput"
                        className="form__input--text"
                        type="text"
                        {...register("summary")} 
                        disabled={disabled} />
                </div>
                <FormError field={errors.summary} message={errors.summary?.message} />

                <div className="form__input">
                    <label className="form__label">Date</label>
                    <input
                        id="dateOfEntryInput"
                        className="form__input--text"
                        type="datetime-local"
                        {...register("dateOfEntry")} 
                        disabled={disabled} />
                </div>
                <FormError field={errors.dateOfEntry} message={errors.dateOfEntry?.message} />


                <div className="form__input">
                    <label className="form__label">Overview</label>
                    <textarea
                        id="overviewInput"
                        className="form__input--textarea"
                        {...register("overview")} 
                        disabled={disabled} />
                </div>
                <FormError field={errors.overview} message={errors.overview?.message} />
                
                <div className="form-child-list">
                    <div className="form-child-list__header">
                        <label className="form__label"><h3>Body</h3></label>
                        {
                            (disabled)?
                                <></>
                            :
                                <button className="form-child-list__add-btn btn-link add-btn" onClick={handleAddBody}><FontAwesomeIcon icon={faPlus} /></button>
                        }
                    </div>

                    {
                        fields.map((field, index)=> (
                            <JournalBodyItemForm key={field.id} 
                                                 index={index} 
                                                 register={register} 
                                                 control={control}
                                                 disabled={disabled}
                                                 errors={errors.journalBodyItems? errors.journalBodyItems[index] : undefined}
                                                 remove={remove} />
                        ))
                    }
                    
                </div>
            </form>
        </div>
    )
}