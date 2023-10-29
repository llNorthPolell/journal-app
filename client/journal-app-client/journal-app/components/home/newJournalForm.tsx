'use client'

import React, { useState } from 'react';
import { JournalForm, JournalSchema } from '../../models/journal';
import { SubmitHandler, useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { useRouter } from 'next/navigation';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faX } from '@fortawesome/free-solid-svg-icons';
import FormError from '../formError';
import { submitJournal } from '../../lib/journalAPI';
import { defaultImgUrl, uploadImage } from '../../lib/cloudStorage';

function NewJournalForm() {
    const router = useRouter();
    const [show, setShow] = useState(false);
    const {
        register,
        handleSubmit,
        formState: { errors, isValid },
        trigger,
        reset,
        getValues
    } = useForm<JournalForm>({
        resolver: zodResolver(JournalSchema)
    });

    const handleClose = () => {
        setShow(false);
        reset();
    }

    const handleShow = (e: React.MouseEvent<HTMLElement>) => {
        e.preventDefault();
        setShow(true);
    };

    const onSubmit: SubmitHandler<JournalForm> = async (journalForm) => {
        reset();
        setShow(false);
        console.log("components/newJournalForm - Submit: " + JSON.stringify(journalForm))
        console.log("components/newJournalForm - Errors:" + JSON.stringify(errors));
        console.log("components/newJournalForm - Is Valid: " + isValid);

        const name = journalForm.name;
        const image =(journalForm.img)?journalForm.img[0] as Blob: undefined;
      
      
        let saveImgUrl : string = defaultImgUrl;
        if (image){
          saveImgUrl = await uploadImage(image) as string;
          console.log("New image URL: " + saveImgUrl);
        }
      
        const journalToSave = {
          name: name,
          img: saveImgUrl
        }

        await submitJournal(journalToSave);
    }


    return (
        <>
            <button className="create-journal-btn btn-link animated-entry-top" onClick={handleShow}> New Journal </button>
            {
                (show) ?
                    <form className="form" onSubmit={handleSubmit(onSubmit)}>
                        <div className="modal">
                            <div className="modal__content animated-entry-top">
                                <div className="modal__header">
                                    <h2 className="heading-tertiary">New Journal</h2>
                                    <button className="modal__closeBtn" onClick={handleClose}>
                                        <h3><FontAwesomeIcon icon={faX} /></h3>
                                    </button>
                                </div>

                                <div className="modal__body">
                                    <div className="form__input">
                                        <label className="form__label">Journal Name</label>
                                        <input
                                            id="journalNameInput"
                                            className="form__input--text"
                                            type="text"
                                            {...register("name")} />
                                    </div>
                                    <FormError field={errors.name} message={errors.name?.message} />

                                    <div className="form__input">
                                        <label className="form__label">Journal Image</label>
                                        <input
                                            id="journalImgField"
                                            className="form__input--file"
                                            type="file"
                                            {...register("img")} />
                                    </div>
                                    <FormError field={errors.img} message={errors.img?.message} />

                                </div>

                                <div className="modal__footer">
                                    <button id="newJournalSubmit" type="submit" className="btn-link form__submit-btn">Create</button>
                                </div>

                            </div>
                        </div>
                    </form>
                    :
                    <></>
            }
        </>

    );
}

export default NewJournalForm;