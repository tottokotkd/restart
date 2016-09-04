/**
 * Created by tottokotkd on 28/08/2016.
 */

import { createStore, applyMiddleware } from 'redux'
import * as ReduxThunk from 'redux-thunk'
import * as ReduxLogger from 'redux-logger'
import {rootReducer} from '../reducers/index'


export default function configureStore(preloadedState) {
    const store = createStore(
        rootReducer,
        preloadedState,
        applyMiddleware(ReduxThunk.default, ReduxLogger())
    )
    return store
}
