import random
import sys
import time
from pprint import pprint
from urllib.parse import urljoin

import requests


def simulate(base: str):
    with requests.Session() as session:
        with session.post(url=urljoin(base, '/player/register'), json={
            "username": "leon",
            "password": "leon",
            "email": "test@test.com",
            "weight": 58,
            "age": 18
        }) as r:
            print('>>> Trying to register user...')
            pprint(r.json())
            time.sleep(1)

        with session.post(url=urljoin(base, '/player/login'), json={
            "username": "leon",
            "password": "leon"
        }) as r:
            print('>>> Trying to login...')
            pprint(r.json())
            time.sleep(1)

        with session.post(url=urljoin(base, '/hrm/start'), json={
            "username": "leon",
            "heartRate": 0
        }) as r:
            print('>>> Trying to start HRM...')
            pprint(r.text)
            time.sleep(1)

        x, y = (12, 13)

        with session.post(url=urljoin(base, '/game/start'), json={
            "username": "leon",
            "zone": "mac",
            "location": {
                "x": x,
                "y": y,
            }
        }) as r:
            print(f'>>> Starting game in Zone: MAC ({x}, {y})')
            pprint(data := r.json())
            shelters = data['data']['shelters']
            time.sleep(1)

        for i in range(10):
            time.sleep(2)
            x, y = x + random.randint(1, 10), y + random.randint(1, 10)
            action = random.choice(('Escaping', 'Fighting', 'Sheltering'))
            with session.post(url=urljoin(base, '/game/action'), json={
                "username": "leon",
                "action": action,
                "type": "Cardio",
                "location": {
                    "x": x,
                    "y": y,
                }
            }) as r:
                print(f'>>> Make random game action: {action}')
                print(f'>>> Movie to next location: ({x}, {y})')
                pprint(r.json())
                time.sleep(1)

        with session.post(url=urljoin(base, '/game/stop'), json={
            "username": "leon"
        }) as r:
            print('Stopping game...')
            pprint(r.json())
            time.sleep(1)

        with session.post(url=urljoin(base, '/hrm/stop'), json={
            "username": "leon",
            "heartRate": 0
        }) as r:
            print('>>> Trying to stop HRM...')
            pprint(r.text)
            time.sleep(1)

        with session.post(url=urljoin(base, '/player/logout'), json={
            "username": "leon"
        }) as r:
            print('>>> Trying to logout...')
            pprint(r.json())


def main():
    if len(sys.argv) != 2:
        print(f'Usage: {sys.argv[0]} <host:port>')
        return

    addr = sys.argv[1]
    if not addr.startswith('http://'):
        addr = f'http://{addr}'

    simulate(addr)


if __name__ == '__main__':
    main()
