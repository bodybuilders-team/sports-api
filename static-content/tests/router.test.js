import {Router} from "../router.js";
import {changeStatePath, createState} from "../js/compLib.js";
import {a, p} from "../js/dom/domTags.js";
import {LogError} from "../js/errorUtils.js";

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
                const state = createState()
                changeStatePath(state, '/')

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
                const state = createState()
                changeStatePath(state, 'dada/')

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
                const state = createState()
                changeStatePath(state, 'dada/')

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
                assert.strictEqual(state.currentPath, 'dada/');
            });

        })

        describe('Handler path', () => {
            it('with valid path is routed properly', async () => {
                const state = createState()
                changeStatePath(state, '/Hello/World/dada')

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
                const state = createState()
                changeStatePath(state, '/Hello/Word')

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
                const state = createState()
                changeStatePath(state, '/Hello/World/dada')

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
                const state = createState()
                changeStatePath(state, '/Hello/')

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


        describe('Handler path with query', () => {
            it('and with valid path is routed properly', async () => {
                const state = createState()
                changeStatePath(state, '/Hello/World/dada?Hello=6&yee=5')

                const router = Router();

                router.addHandler('/Hello/World', async (state, props) => {
                    return p("Hello world");
                });

                const generatedElement = await router(state);

                assert.isDefined(generatedElement);
                assert.strictEqual(generatedElement.tagName, 'P');

                assert.strictEqual(state.currentPath, '/dada?Hello=6&yee=5');
                assert.strictEqual(state.query.Hello, '6');
                assert.strictEqual(state.query.yee, '5');
            });

            it('and with invalid path throws LogError without default handler', async () => {
                const state = createState()
                changeStatePath(state, '/Hello?Hello=5/dada')

                const router = Router();

                router.addHandler('/Hello/dada', async (state, props) => {
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