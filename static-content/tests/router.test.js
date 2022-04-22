import {Router} from "../routers/router.js";
import {a, p} from "../js/dom/domTags.js";
import {LogError} from "../js/errorUtils.js";
import {createState} from "../js/compLib.js";

const assert = chai.assert

describe('Router', () => {
    it('Should be a function', () => {
        assert.typeOf(Router, 'function');
    });

    it('Generated component should be a function', () => {
        assert.typeOf(Router(), 'function');
    });

    it('Generated component is always a new function object', () => {
        const router = Router();
        const router2 = Router();

        assert.notStrictEqual(router, router2);
    });

    describe('Routing tests', () => {
        describe('Simple handler path', () => {

            it('with valid path is routed properly', async () => {
                const state = createState("/")

                const router = Router();

                router.addHandler('/', async (state, props) => {
                    return p("Hello world");
                });

                const generatedElement = await router(state);

                assert.isDefined(generatedElement);
                assert.strictEqual(generatedElement.tagName, 'P');
                assert.strictEqual(state.currentPath, '');
            });

            it('with invalid path throws LogError without default handler', async () => {
                const state = createState('dada/')

                const router = Router();

                router.addHandler('/', async (state, props) => {
                    return p("Hello world");
                });

                try {
                    await router(state);
                    assert.fail("router doesn't throw Log Error")
                } catch (e) {
                    assert.isDefined(e);
                    assert.instanceOf(e, LogError);
                }
            });

            it('with invalid path returns default handler', async () => {
                const state = createState('dada/')

                const router = Router();

                router.addHandler('/', async (state, props) => {
                    return p("Hello world");
                });

                router.addDefaultHandler(async (state, props) => {
                    return a("Default handler");
                });

                const ele = await router(state);

                assert.isDefined(ele);
                assert.strictEqual(ele.tagName, 'A');
                assert.strictEqual(state.currentPath, '/dada/');
            });

        })

        describe('Handler path', () => {
            it('with valid path is routed properly', async () => {
                const state = createState('/Hello/World/dada')

                const router = Router();

                router.addHandler('/Hello/World', async (state, props) => {
                    return p("Hello world");
                });

                const generatedElement = await router(state);

                assert.isDefined(generatedElement);
                assert.strictEqual(generatedElement.tagName, 'P');
                assert.strictEqual(state.currentPath, '/dada');
            });

            it('with invalid path throws LogError without default handler', async () => {
                const state = createState('/Hello/Word')

                const router = Router();

                router.addHandler('/Hello/World', async (state, props) => {
                    return p("Hello world");
                });

                try {
                    await router(state);
                    assert.fail("router doesn't throw Log Error")
                } catch (e) {
                    assert.isDefined(e);
                    assert.instanceOf(e, LogError);
                }
            });
        })

        describe('Handler path with params', () => {
            it('and with valid path is routed properly', async () => {
                const state = createState('/Hello/World/dada')

                const router = Router();

                router.addHandler('/Hello/:id', async (state, props) => {
                    return p("Hello world");
                });

                const generatedElement = await router(state);

                assert.isDefined(generatedElement);
                assert.strictEqual(generatedElement.tagName, 'P');

                assert.strictEqual(state.params.id, 'World');
                assert.strictEqual(state.currentPath, '/dada');
            });

            it('and with invalid path throws LogError without default handler', async () => {
                const state = createState('/Hello/')

                const router = Router();

                router.addHandler('/Hello/:id', async (state, props) => {
                    return p("Hello world");
                });

                try {
                    await router(state);
                    assert.fail("router doesn't throw Log Error")
                } catch (e) {
                    assert.isDefined(e);
                    assert.instanceOf(e, LogError);
                }
            });
        })

    })
})